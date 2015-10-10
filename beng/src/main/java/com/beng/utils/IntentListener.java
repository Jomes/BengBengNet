package com.beng.utils;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 跳转接口
 * 
 * @author jomeslu
 * 
 */
public interface IntentListener {
	/**
	 * 往容器里面添加fragment
	 * 
	 * @param contentID
	 *            容器ID
	 * @param newFragment
	 *            碎片
	 * @param addToBackStack
	 *            是否保存堆栈信息
	 */
	public void addFragment(int contentID, Fragment newFragment,
                            boolean addToBackStack);

	/**
	 * fragment替换
	 * 
	 * @param contentID
	 *            容器ID
	 * @param newFragment
	 *            碎片
	 * @param addToBackStack
	 *            是否保存堆栈信息
	 */
	public void replaceFragment(int contentID, Fragment newFragment,
                                boolean addToBackStack);

	/**
	 * 去裁剪图片
	 * 
	 * @param path
	 *            图片地址
	 * @param requestUri
	 *            裁剪回调地址
	 * @param size
	 *            裁剪大小
	 */
	public void goToCropImage(String path, Uri requestUri, int size);

	/**
	 * 通过地址查看图片
	 * 
	 * @param path
	 *            图片地址
	 */
	public void goToView(String path);

	/**
	 * 通过地址查看图片
	 * 
	 * @param path
	 *            图片地址
	 */
	public void goToView(String path, Class<?> cls);

	/**
	 * 单纯的页面跳转
	 * 
	 * @param cls
	 *            跳转的页面
	 */
	public void goToOthers(Class<?> cls);

	/**
	 * 页面跳转并关闭当前页面
	 * 
	 * @param cls
	 *            跳转的页面
	 */
	public void goToOthersF(Class<?> cls);

	/**
	 * 带参数的页面跳转
	 * 
	 * @param cls
	 *            跳转的页面
	 * @param bundle
	 *            参数
	 */
	public void goToOthers(Class<?> cls, Bundle bundle);

	/**
	 * 带参数的页面跳转并关闭当前页面
	 * 
	 * @param cls
	 *            跳转的页面
	 * @param bundle
	 *            参数
	 */
	public void goToOthersF(Class<?> cls, Bundle bundle);

	/**
	 * 带回调的页面跳转
	 * 
	 * @param cls
	 *            跳转的页面
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            请求码
	 */
	public void goToOthersForResult(Class<?> cls, Bundle bundle, int requestCode);

	/**
	 * 设置回调
	 * 
	 * @param cls
	 *            回调的页面
	 * @param bundle
	 *            参数
	 * @param resultCode
	 *            返回码
	 */
	public void backForResult(Class<?> cls, Bundle bundle, int resultCode);

	/**
	 * 让某一页面顶置
	 * 
	 * @param bundle
	 *            参数
	 */
	public void upToHome(Class<?> cls, Bundle bundle);

	/**
	 * 让某一页面顶置
	 */
	public void upToHome(Class<?> cls);

	/**
	 * 模拟home键事件
	 */
	public void homeAction();

	/**
	 * 跳转到网页
	 * 
	 * @param url
	 *            网页地址
	 */
	public void goToWeb(String url);

	/**
	 * 打电话
	 * 
	 * @param telePhoneNum
	 *            电话号码
	 */
	public void goToCall(String telePhoneNum);

	/**
	 * 安装应用
	 * 
	 * @param file
	 */
	public void installApp(File file);

	/**
	 * 去照相
	 * 
	 * @param file
	 *            照相文件
	 */
	public void goToPhoto(File file);

	/**
	 * 去图库
	 */
	public void goToGallery();

}
