package com.beng.base.core;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beng.R;
import com.beng.utils.IntentListener;
import com.beng.view.MyProgressBar;

import java.io.File;


/**
 * FragmentActivity基类
 * 
 * @author jomeslu
 * 
 */
public class BaseFragmentActivity extends FragmentActivity implements
        OnClickListener, IntentListener {

    /**
     * 参数传递标示
     */
    public static final String PARAM_INTENT = "intentData";
    /**
     * 传递参数
     */
    private Bundle intentData;

    public boolean isHasFragment = false;
    /*** true 统一在最外层添加一个parentView ****/
    protected boolean isHasContainer = false;
    private IntentListener intentFactory;
    protected Activity  mContext;

    /******** 屏幕宽高 **********/
    public int screenWidth, screenHeight;
    protected MyProgressBar mProgressBar;

    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            intentData = getIntent().getExtras();
        } else {
            intentData = savedInstanceState.getBundle(PARAM_INTENT);
        }

        mProgressBar = new MyProgressBar(this);
        Bundle bundle = intentData != null
                && intentData.getBundle(PARAM_INTENT) != null ? intentData
                .getBundle(PARAM_INTENT) : intentData;
        initIntentData(bundle != null ? bundle : new Bundle());
        super.onCreate(savedInstanceState);
        intentFactory = new ActivityIntentFactory(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        mContext=this;
        setBundleView(bundle);
    }

    
    
    public void setBundleView(Bundle bundle){
      
    }

//    public void setContentView(int layoutResID) {
//
//        if (isHasContainer) {
//            super.setContentView(R.layout.lib_container);
//            View view = LayoutInflater.from(this).inflate(layoutResID, null);
//            LinearLayout ll_container = (LinearLayout) findViewById(
//                    R.id.ll_container);
//            ll_container.addView(view, LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT);
//        } else {
//            super.setContentView(layoutResID);
//        }
//    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case android.R.id.home:
            initBackButton();
            return true;
        default:
            menuListener(item);
            return super.onOptionsItemSelected(item);
        }
    }

    public void menuListener(MenuItem item) {

    }

    /**
     * 初始化返回按钮
     */
    public void initBackButton() {

        finish();
    }

    protected void onDestroy() {

        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {

        outState.putBundle(PARAM_INTENT, intentData);
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化页面传递过来的数据
     * 
     * @param bundle
     *            数据
     */
    protected void initIntentData(Bundle bundle) {

    }

    /**
     * 限制EditText输入，最长不能超过length的长度
     * 
     * @param et
     *            EditText控件
     * @param length
     *            限制长度
     */
    public void limitEditTextLength(EditText et, int length) {

        et.addTextChangedListener(new MyTextWatcher(et, length));
    }

    private class MyTextWatcher implements TextWatcher {

        private EditText et;
        private int length;

        public MyTextWatcher(EditText et, int length) {

            this.et = et;
            this.length = length;
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {

        }

        public void afterTextChanged(Editable s) {

            String mText = et.getText().toString();
            int len = mText.length();
            if (len > length) {
                mText = mText.substring(0, length);
                et.setText(mText);
            }
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideInputWindow() {

        View focus = getCurrentFocus();
        if (focus != null) {
            IBinder focusBinder = focus.getWindowToken();
            if (focusBinder != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(focusBinder,
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     * 
     * @param mEditText
     */
    protected void showInputWindow(EditText mEditText) {

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(mEditText, 0);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle = null;
        if (data != null) {
            bundle = data.getBundleExtra(PARAM_INTENT);
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

    public void onClick(View v) {

    }

}
