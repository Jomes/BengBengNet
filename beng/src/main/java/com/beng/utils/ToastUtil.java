package com.beng.utils;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beng.MyApplication;
import com.beng.R;


/**
 * Toast工具类
 *
 * @author wjy
 */
public class ToastUtil {
    /**
     * Toast
     *
     * @param msg 消息
     */
    public static void toast(final String msg) {
        Handler handler = new Handler(MyApplication.getInstance().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(MyApplication.getInstance(),
                        Tools.trim(msg), Toast.LENGTH_SHORT);
                View view = LayoutInflater.from(MyApplication.getInstance()).inflate(
                        R.layout.lib_view_toast, null);
                TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
                tv_toast.setText(msg);
                toast.setView(view);
                toast.show();
            }
        });

    }

    public static void toast(final String msg, final int length) {
        Handler handler = new Handler(MyApplication.getInstance().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(MyApplication.getInstance(),
                        Tools.trim(msg), length);
                View view = LayoutInflater.from(MyApplication.getInstance()).inflate(
                        R.layout.lib_view_toast, null);
                TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
                tv_toast.setText(msg);
                toast.setView(view);
                toast.show();
            }
        });

    }

    /**
     * Toast
     *
     * @param resId 消息ID
     */
    public static void toast(int resId) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), resId,
                Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(
                R.layout.lib_view_toast, null);
        TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
        tv_toast.setText(resId);
        toast.setView(view);
        toast.show();
    }

    public static void toast(int resId, int length) {
        Toast toast = Toast
                .makeText(MyApplication.getInstance(), resId, length);
        View view = LayoutInflater.from(MyApplication.getInstance()).inflate(
                R.layout.lib_view_toast, null);
        TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
        tv_toast.setText(resId);
        toast.setView(view);
        toast.show();
    }
}
