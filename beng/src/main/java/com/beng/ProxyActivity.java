package com.beng;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beng.base.BaseActivity;


/**
 * Created by jomeslu on 15-8-6.
 */
public class ProxyActivity extends BaseActivity {

    private Bundle bundle;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proxy, container,
                false);
        int code = bundle.getInt(Constants.intent_mode);
//        replaceFragmentByMode(code);
        return view;

    }


    @Override
    public void initIntentData(Bundle bundle) {
        this.bundle = bundle;
    }

//    private void replaceFragmentByMode(int code) {
//        switch (code) {
//            case Constants.proxy_setting:
//                replaceFragment(R.id.activity_container, new SettingFragment(), false);
//                break;
//
//
//        }
//
//    }


}
