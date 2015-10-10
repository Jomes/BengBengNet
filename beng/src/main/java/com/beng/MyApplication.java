package com.beng;

import android.app.Application;
import android.text.TextUtils;

import com.beng.http.OnBindAndAppoinmentListener;
import com.sohu.focus.framework.loader.FocusResponseError;
import com.sohu.focus.framework.util.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyApplication extends Application {
    private static MyApplication instance;
    private HashMap<String, OnBindAndAppoinmentListener> mBindList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mBindList = new HashMap<String, OnBindAndAppoinmentListener>();

    }


    public static MyApplication getInstance() {
        return instance;
    }


    /**
     * 在oncreate调用
     *
     * @param mOnBindAndAppoinmentListener
     */
    public void registBindAndAppoinmentListener(
            OnBindAndAppoinmentListener mOnBindAndAppoinmentListener) {
        if (mOnBindAndAppoinmentListener != null) {
            mBindList.put(mOnBindAndAppoinmentListener.getClass().toString(), mOnBindAndAppoinmentListener);
//      mBindList.add(mOnBindAndAppoinmentListener);
        }
    }

    /**
     * 在detory 调用
     *
     * @param mOnBindAndAppoinmentListener
     */
    public void unRegisterBindAndAppoinmentListener(
            OnBindAndAppoinmentListener mOnBindAndAppoinmentListener) {
        if (mOnBindAndAppoinmentListener != null) {
            mBindList.remove(mOnBindAndAppoinmentListener.getClass().toString());
        }
    }


    public void onBindAndAppoinmentSuccess(Object result, int mode) {
        LogUtils.i("jomeslu", "HashMap的大小：" + mBindList.size());
        Iterator<Map.Entry<String, OnBindAndAppoinmentListener>> iter = mBindList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, OnBindAndAppoinmentListener> bindle = iter.next();
            bindle.getValue().onBindResult(result, mode);
            LogUtils.i("jomeslu", "HashMap Key：" + bindle.getKey());
        }

    }


    //验证时间
    public String authorTime;
    public String authorID;
    public String bengLoginId;
    public String userHit;
    public String phpSessid;

    public void setAuthorTime(String authorTime) {

        this.authorTime = authorTime;
    }

    public void setBengLoginId(String bengLoginId) {

        this.bengLoginId = bengLoginId;
    }

    public void setAuthorID(String authorID) {

        this.authorID = authorID;
    }

    public void setUserHit(String userHit) {

        this.userHit = userHit;
    }

    public void setPhpSessid(String phpSessid) {

        this.phpSessid = phpSessid;
    }


}
