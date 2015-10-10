package com.beng.base.core;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.beng.base.BaseActivity;
import com.beng.base.ILoadingViewStateFactory;
import com.beng.utils.IntentListener;


/**
 * Fragment基类
 * 
 * @author jomeslu
 * 
 */
public class BaseFactoryFragment extends Fragment implements OnClickListener,
        IntentListener {

	public static boolean DEBUG = false;

	private IntentListener intentFactory;
	protected ILoadingViewStateFactory mILoadingView;
	private BaseActivity baseActivity;
	protected Activity mContext;
	
	public void onAttach(Activity activity) {

		if (DEBUG)
			System.out.println("fragment开始onAttach");
		super.onAttach(activity);
		mContext=activity;
	}

	public void onCreate(Bundle savedInstanceState) {

		if (DEBUG)
			System.out.println("fragment开始onCreate");
		super.onCreate(savedInstanceState);
		intentFactory = new FragmentIntentFactory(this);
		mILoadingView=new ILoadingViewStateFactory(mContext);
		baseActivity = getBaseActivity();
		baseActivity.isHasFragment = true;
	}
	
//	   /**
//     * 初始化页面传递过来的数据
//     * 
//     * @param bundle
//     *            数据
//     */
//    protected void initIntentData(Bundle bundle) {
//
//    }


	/**
	 * 限制EditText输入，最长不能超过length的长度
	 * 
	 * @param et
	 *            EditText控件
	 * @param length
	 *            限制长度
	 */
	public void limitEditTextLength(final EditText et, final int length) {

		if (baseActivity != null)
			baseActivity.limitEditTextLength(et, length);
	}

	/**
	 * 往容器里面添加fragment
	 * 
	 */
	public void addFragment(int contentID, Fragment newFragment,
			boolean addToBackStack) {

		intentFactory.addFragment(contentID, newFragment, addToBackStack);
	}

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
			boolean addToBackStack) {

		intentFactory.replaceFragment(contentID, newFragment, addToBackStack);
	}

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
	public void goToCropImage(String path, Uri requestUri, int size) {

		intentFactory.goToCropImage(path, requestUri, size);
	}

	public void goToView(String path) {

		intentFactory.goToView(path);
	}

	/**
	 * 通过地址查看图片
	 * 
	 * @param path
	 *            图片地址
	 */
	public void goToView(String path, Class<?> cls) {

		intentFactory.goToView(path, cls);
	}

	/**
	 * 单纯的页面跳转
	 * 
	 * @param cls
	 *            跳转的页面
	 */
	public void goToOthers(Class<?> cls) {

		intentFactory.goToOthers(cls);
	}

	/**
	 * 页面跳转并关闭当前页面
	 * 
	 * @param cls
	 *            跳转的页面
	 */
	public void goToOthersF(Class<?> cls) {

		intentFactory.goToOthersF(cls);
	}

	/**
	 * 带参数的页面跳转
	 * 
	 * @param cls
	 *            跳转的页面
	 * @param bundle
	 *            参数
	 */
	public void goToOthers(Class<?> cls, Bundle bundle) {

		intentFactory.goToOthers(cls, bundle);
	}

	/**
	 * 带参数的页面跳转并关闭当前页面
	 * 
	 * @param cls
	 *            跳转的页面
	 * @param bundle
	 *            参数
	 */
	public void goToOthersF(Class<?> cls, Bundle bundle) {

		intentFactory.goToOthersF(cls, bundle);
	}

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
	public void goToOthersForResult(Class<?> cls, Bundle bundle, int requestCode) {

		intentFactory.goToOthersForResult(cls, bundle, requestCode);
	}

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
	public void backForResult(Class<?> cls, Bundle bundle, int resultCode) {

		intentFactory.backForResult(cls, bundle, resultCode);
	}

	/**
	 * 让某一页面顶置
	 * 
	 * @param bundle
	 *            参数
	 */
	public void upToHome(Class<?> cls, Bundle bundle) {

		intentFactory.upToHome(cls, bundle);
	}

	/**
	 * 让某一页面顶置
	 */
	public void upToHome(Class<?> cls) {

		intentFactory.upToHome(cls);
	}

	public void homeAction() {

		intentFactory.homeAction();
	}

	/**
	 * 跳转到网页
	 * 
	 * @param url
	 *            网页地址
	 */
	public void goToWeb(String url) {

		intentFactory.goToWeb(url);
	}

	/**
	 * 打电话
	 * 
	 * @param telePhoneNum
	 *            电话号码
	 */
	public void goToCall(String telePhoneNum) {

		intentFactory.goToCall(telePhoneNum);
	}
	
	/**
	 * 安装应用
	 * 
	 * @param file
	 */
	public void installApp(File file) {

		intentFactory.installApp(file);
	}

	public void goToPhoto(File file) {

		intentFactory.goToPhoto(file);
	}

	public void goToGallery() {

		intentFactory.goToGallery();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bundle bundle = null;
		if (data != null) {
			bundle = data.getBundleExtra(BaseActivity.PARAM_INTENT);
			if (bundle == null) {
				bundle = data.getExtras();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		onActivityResult(requestCode, resultCode, bundle);
	}

	/**
	 * 页面回调函数
	 * 
	 * @param requestCode
	 *            请求码
	 * @param resultCode
	 *            返回码
	 * @param data
	 *            数据
	 */
	protected void onActivityResult(int requestCode, int resultCode, Bundle data) {

	}

	public void onActivityCreated(Bundle savedInstanceState) {

		if (DEBUG)
			System.out.println("fragment开始onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	public void onStart() {

		if (DEBUG)
			System.out.println("fragment开始onStart");
		super.onStart();
	}

	public void onResume() {

		if (DEBUG)
			System.out.println("fragment开始onResume");
		super.onResume();
	}

	public void onPause() {

		if (DEBUG)
			System.out.println("fragment开始onPause");
		super.onPause();
	}

	public void onDestroyView() {

		if (DEBUG)
			System.out.println("fragment开始onDestroyView");
		super.onDestroyView();
	}

	public void onDestroy() {

		if (DEBUG)
			System.out.println("fragment开始onDestroy");
		super.onDestroy();
	}

	public void onDetach() {

		if (DEBUG)
			System.out.println("fragment开始onDetach");
		super.onDetach();
	}

	public void onClick(View v) {

	}

	/**
	 * 获取Activity基类对象
	 * 
	 * @return BaseActivity对象
	 */
	public BaseActivity getBaseActivity() {

		try {
			return (BaseActivity) getActivity();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
