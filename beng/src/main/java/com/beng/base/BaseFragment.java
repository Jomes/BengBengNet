package com.beng.base;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.beng.base.core.BaseFactoryFragment;


/**
 * @author jomeslu
 */
public class BaseFragment extends BaseFactoryFragment {

    protected View mRefreshView;
    protected View mFailedView;
    protected View mSucceedView;
    protected Animation displayanimation;
    protected Animation dismissanimation;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		initTitle();
        displayanimation = new AlphaAnimation(0.1f, 1.0f);
        displayanimation.setDuration(500);
        dismissanimation = new AlphaAnimation(1.0f, 0.1f);
        dismissanimation.setDuration(500);

    }


    @Override
    public void onResume() {

        super.onResume();
    }


    @Override
    public void onPause() {

        super.onPause();
    }


}
