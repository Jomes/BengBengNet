package com.sohu.focus.framework.upgrade;

import java.lang.ref.WeakReference;

import android.content.Context;

import com.sohu.focus.framework.util.LogUtils;
/**
 * 
 * @author jomeslu
 *
 */
public class LibEvent {


	private static LibEvent instance;

	private LibEvent() {};

	private   WeakReference<LibEventCallBackListener> mWeakReference;

	public static synchronized LibEvent getInstance(Context mContext) {

		if (instance == null) {
			LogUtils.i("test", "初始化单例");
			instance = new LibEvent();
		}

		return instance;

	}

	public void registEventManagerListener(LibEventCallBackListener mOnEventCallBackListener) {
		if (mOnEventCallBackListener != null) {
			LogUtils.i("test", "注册时间，");
			mWeakReference = new WeakReference<LibEventCallBackListener>(mOnEventCallBackListener);
		}
	}

	public void unRegistEventManagerListener() {
		if (mWeakReference != null) {
			mWeakReference.clear();
		}

	}

	public void notifyEvent(Object obj, int code) {
		if (mWeakReference != null&&mWeakReference.get()!=null) {
			LogUtils.i("test", "进度回调");
			mWeakReference.get().onEventResult(obj, code);
		}

	}

}
