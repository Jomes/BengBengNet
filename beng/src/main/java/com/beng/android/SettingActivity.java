package com.beng.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beng.R;
import com.beng.base.BaseActivity;
import com.beng.view.CustomerDialog;

/**
 * Created by Jomelu on 15/10/4.
 */
public class SettingActivity extends BaseActivity {
    private TextView mTextView;
    private EditText mbeishu;
    private TextView leftView;
    private TextView centerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.mode);
        leftView = (TextView) findViewById(R.id.title_left);
        centerView = (TextView) findViewById(R.id.title_center);
        mbeishu = (EditText) findViewById(R.id.beishu);
        mTextView.setOnClickListener(this);
        leftView.setOnClickListener(this);
        centerView.setText("投注设置");

    }

    private CustomerDialog mCustomerDialog;
    private String[] str = {"翻倍模式", "倒秒模式"};

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.mode:
                if (mCustomerDialog == null) {
                    mCustomerDialog = new CustomerDialog(mContext, str);
                    mCustomerDialog.setOnBtnClickListener(new CustomerDialog.OnBtnClickListener() {
                        @Override
                        public void onbtnOk(String mBaseDialogMode) {

                            mTextView.setText(mBaseDialogMode);

                        }
                    });
                }
                mCustomerDialog.show();

                break;
            case R.id.title_left:
                mContext.finish();

                break;
        }
    }
}
