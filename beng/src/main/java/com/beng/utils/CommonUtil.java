package com.beng.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 不跟业务逻辑相关，可供所有项目使用的通用工具类
 *
 * @author jomeslu
 */
public class CommonUtil {
    /**
     * 判断是否魅族手机
     *
     * @return
     */
    public static boolean isMX3() {
        String brand = CommonUtil.getBrand();
        if ("Meizu".equalsIgnoreCase(brand) && Build.VERSION.SDK_INT == 17) {
            return true;
        }
        return false;
    }

    public static boolean notEmpty(String str) {
        return (str != null && !str.equals("") && !str.equals("null") && !str.equals("　") && !str
                .equals(" "));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("　") || str.equals(" ")
                || str.equals("null");
    }


    public static String getImei(Context context){
        String imei="";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imei;
    }


    // 获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    public static int getExpiredTime_5Day() {
        return 5 * 24 * getExpiredTime_1Hour();
    }

    public static int getExpiredTime_1Hour() {
        return 60 * 60 * 1000;
    }

    /**
     * 获取当前的sdk版本
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }


    /**
     * 设置 textView的Drawable
     *
     * @param context
     * @param view
     * @param icon
     * @param status  1表示左边 2表示右边 3表示不设置
     * @return
     */
    public static TextView setTextDrawable(Context context, TextView view, int icon, int status) {
        Drawable drawable = context.getResources().getDrawable(icon);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (status) {
            case 1:
                view.setCompoundDrawables(drawable, null, null, null);
                break;
            case 2:
                view.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                view.setCompoundDrawables(null, null, null, null);
                break;
        }

        return view;
    }

    /**
     * 去除父类的引用
     *
     * @param view
     */
    public static void removeFromSuperView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
    }

    /**
     * 得到车船使用税的数组 规则是： 获取手机系统年份 比如 2013年 显示规则是： Item-0 已过期 Item-1 2012 Item-2 2011
     */
    public static String[] getVericalTaxTimeArray() {
        // 获取手机里的年份
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(new java.util.Date()));
        String year0 = "已过期";
        String year1 = String.valueOf((year + 0));
        String year2 = String.valueOf((year + 1));
        String[] arr = {year0, year1, year2};
        return arr;
    }

    /**
     * 获得当前手机操作系统的版本
     *
     * @return 设备系统版本
     */
    public static int getOSVersion() {

       return  Build.VERSION.SDK_INT;

    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 手机型号
     *
     * @return 手机型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获得应用版本号
     *
     * @param context
     * @return app版本号
     */
    public static String getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(versionCode);
    }

    /**
     * 获得应用版本名称
     *
     * @param context
     * @return app版本名称
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        if (context != null) {
            try {
                versionName =
                        context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
                                .toString();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    /**
     * 当前设备密度
     *
     * @param context
     * @return 当前设备屏幕密度 （像素比例：0.75/1.0/1.5/2.0）
     */
    public static double getScreenDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.density / 1.5;
        }
        return 1;// 默认为480*800
    }

    /**
     * 当前设备密度
     *
     * @param context
     * @return 当前设备屏幕密度（每寸像素：120/160/240/320）
     */
    public static int getScreenDensityDpi(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.densityDpi;
        }
        return DisplayMetrics.DENSITY_DEFAULT;
    }

    /**
     * 得到设备宽度
     *
     * @param context
     * @return 当前设备屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 480;
    }

    /**
     * 得到设备高度
     *
     * @param context
     * @return 设备高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 800;
    }

    /**
     * 保留两位小数点
     *
     * @param num 待格式化数字
     * @return 格式化后的字符串
     */
    public static String formatFloat2Point(float num) {
        String f1 = "";
        try {
            DecimalFormat fnum = new DecimalFormat("0.00");
            f1 = fnum.format(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f1;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp(像素)
     */
    public static int px2Dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取本机ip (gprs)
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    /**
     * Wifi 获取ip
     *
     * @param i
     * @return
     */
    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    /**
     * 将图片处理为圆角
     *
     * @param bitmap 要改变的图片
     * @param pixels 圆角角度
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        float roundPx;
        if (bitmap == null) {
            return bitmap;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        if (output == null) {
            return null;
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        if (pixels != 0) {
            roundPx = bitmap.getWidth() / pixels;
        } else {
            roundPx = 0;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 图片转变为字节流
     *
     * @param bitmap      要改变的图片
     * @param needRecycle 是否需要回收
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bitmap, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 70, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 图片缩放到指定长宽
     *
     * @param bitmap 待缩放图片
     * @param w      指定的宽度
     * @param h      指定的高度
     * @return 缩放后的bitmap
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 如果图片小于
        if (width < 800 || height < 600) {
            return bitmap;
        }

        int newWidth = w;
        int newHeight = h;
        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        // recreate the new Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return bitmap;
    }

    /**
     * 转字符串
     *
     * @param mContext
     * @param str
     * @param value1
     * @param value2
     * @return
     */
    public static String pareStr(Context mContext, int str, String value1, String value2) {

        String agentStr = mContext.getString(str);
        value2 = notEmpty(value2) ? value2 : "";
        value1 = notEmpty(value1) ? value1 : "";
        String formatStr = String.format(agentStr, value1, value2);

        return formatStr;
    }

    public static String pareStr(Context mContext, int str, String value) {


        String agentStr = mContext.getString(str);
        value = notEmpty(value) ? value : "";
        String formatStr = String.format(agentStr, value);
        return formatStr;
    }


    public static String getPhone(Context context) {
        String phone = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            phone = tm.getLine1Number();//获取本机号码
            if (notEmpty(phone) && phone.length() > 11) {
                phone = phone.substring(phone.length() - 11,
                        phone.length());
            }
        } catch (Exception e) {
        }
        return phone;


    }

    /**
     * 保存bitmap到指定路径
     *
     * @param bitmap  待保存的bitmap
     * @param imgPath 保存到sd卡的路径(全路径)
     */
    public static void saveBitmapToSdCard(Bitmap bitmap, String imgPath) {
        if (bitmap == null) {
            return;
        }
        File f = new File(imgPath);
        try {
            f.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(CompressFormat.JPEG, 70, fout);
        try {
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();

        }
    }

    /**
     * computeSampleSize方法，返回一个合适的options.inSampleSize值
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
                                        int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                               int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound =
                (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    // 防止连续点击
    private static long lastClickTime;

    /**
     * 判断当前点击动是否为连续点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断手机sd卡是否正常可用
     *
     * @return true/false
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
     * 显示 吐司
     *
     * @param context
     * @param s
     */
    public static void makeToast(Context context, String s) {

        makeToast(context, s, Toast.LENGTH_SHORT);
    }

    public static void makeToast(Context context, String s, int length) {

        Toast.makeText(context, s, length).show();
    }

    public static void makeToast(Context context, int stringResId, int length) {

        Toast.makeText(context, stringResId, length).show();
    }

    /**
     * 判断手机正确性
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int typeValue(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static int typeValueToSp(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
