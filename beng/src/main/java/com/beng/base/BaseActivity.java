package com.beng.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.beng.base.core.BaseFragmentActivity;


/**
 *  基类 ，正常来说 所有Activity继承该类
 * @author jomeslu
 * 
 */
public class BaseActivity extends BaseFragmentActivity {


    protected View mRefreshView;
    protected View mFailedView;
    protected View mSucceedView;
    protected AlphaAnimation displayanimation;
    protected AlphaAnimation dismissanimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
		displayanimation = new AlphaAnimation(0.1f, 1.0f);
        displayanimation.setDuration(500);
        dismissanimation = new AlphaAnimation(1.0f, 0.1f);
        dismissanimation.setDuration(500);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}




}
