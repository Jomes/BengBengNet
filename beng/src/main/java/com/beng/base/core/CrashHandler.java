package com.beng.base.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// system default handle exception class
	private UncaughtExceptionHandler defaultHandler;

	private Context context;

	// exception info
	private final Map<String, String> infos = new HashMap<String, String>();

	private final DateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	private static CrashHandler instance = null;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context context) {
		this.context = context;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// set this class as default handler
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && defaultHandler != null) {
			// if this class don't handle, use system default handler
			defaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// exit
			if (ex.toString().indexOf("OutOfMemoryError") < 0) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}

		}
	}

	/**
	 * custom exception handler, collect error info, send error report, etc
	 * 
	 * @param ex
	 * @return true:handle the exception; or return false.
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		new Thread() {
			@Override
			public void run() {
				if (ex.toString().indexOf("OutOfMemoryError") < 0) {
					Looper.prepare();
					Toast.makeText(context, "很抱歉,程序出现异常,即将退出",
							Toast.LENGTH_LONG).show();
					Looper.loop();
				} else
					System.gc();

			}
		}.start();
		Log.e(TAG, "Exception", ex);

		collectDeviceInfo(context);
		saveCrashInfoToFile(ex);

		return true;
	}

	/**
	 * collectDeviceInfo
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "收集包信息时发生错误", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "收集崩溃信息时发生错误", e);
			}
		}
	}

	/**
	 * save info to file
	 * 
	 * @param ex
	 * @return file name
	 */
	private String saveCrashInfoToFile(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "SoHuCrash-" + time + "-" + timestamp + ".txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "收集包信息时发生错误", e);
		}
		return null;
	}
}