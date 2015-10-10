package com.sohu.focus.framework.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpResponseException;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by miaogao on 13-6-19.
 */
public class UpgradeDownloadService extends Service  {

    public static final String INTENT_EXTRA_URL = "sohu_focus_url";
    public static final String INTENT_EXTRA_ICONID = "sohu_focus_iconid";

    public static final int NOTIFICATION_ID = 1001;
    public static final int DEFAULT_BUFFER_SIZE = 1048 * 8;
    private static final int CONNECTION_TIMEOUT = 8 * 1000;

    private NotificationCompat.Builder mBuilder = null;
    private NotificationManager nm = null;
    private PendingIntent emptyPendingIntent = null;
    private int lastProgress = 0;
    private int iconId = 0;
    private File file = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(intent==null){
            return START_STICKY;
        }
        final String url = intent.getStringExtra(INTENT_EXTRA_URL);
        iconId = intent.getIntExtra(INTENT_EXTRA_ICONID, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                startDowload(url);
            }
        }).start();

        return START_STICKY;
    }

    private void startDowload(String url) {
        FileOutputStream fos = null;
        InputStream is = null;
        int sum = 0;
        int count = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        File dir = getDir("upgrade", Context.MODE_PRIVATE);
        file = new File(dir, "upgrade.apk");

        try {
            URL downloadUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(0);
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new HttpResponseException(responseCode, "HTTP Connection failed with: " + url
                        + " response code: " + responseCode);
            }

            int length = connection.getContentLength();
            is = connection.getInputStream();
            fos = new FileOutputStream(file);
            showStartNotification();
            while ((count = is.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                fos.write(buffer, 0, count);
                fos.flush();
                sum += count;
                updateNotification(sum, length);
            }

            Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
            openApk(file);
            showSuccessNotification();
            LibEvent.getInstance(getApplication()).notifyEvent(null, 3);
            stopSelf();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            showFailNotification();
        } catch (IOException e) {
            e.printStackTrace();
            showFailNotification();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
        }
    }

    private void openApk(File apkfile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkfile.getAbsoluteFile()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
    
    private void showStartNotification() {
        emptyPendingIntent =
                PendingIntent.getActivity(this, 0, new Intent(Intent.ACTION_VIEW),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(iconId).setContentTitle("正在下载升级文件").setContentText("等待中")
                .setProgress(100, 0, false).setContentIntent(emptyPendingIntent);
        nm.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void showSuccessNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = Uri.fromFile(file.getAbsoluteFile());
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(iconId).setContentTitle("下载完成").setContentText("点击开始升级")
                .setContentIntent(pendingIntent).setAutoCancel(true);
        nm.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void showFailNotification() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(iconId).setContentTitle("下载失败").setContentText("请稍后重试")
                .setAutoCancel(true).setContentIntent(emptyPendingIntent);
        if(nm == null)
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, mBuilder.build());
        LibEvent.getInstance(getApplication()).notifyEvent(null, 2);
    }

    private void updateNotification(int sum, int length) {
        int progress = (int) Math.floor(sum * 100 / length);
        if (progress - lastProgress > 3) {
            lastProgress = progress;
            mBuilder.setProgress(100, progress, false);
            mBuilder.setContentText(progress + "%");
            nm.notify(NOTIFICATION_ID, mBuilder.build());
            LibEvent.getInstance(getApplication()).notifyEvent(progress, 1);
           
        }
    }
}
