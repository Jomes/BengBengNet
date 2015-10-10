package com.beng.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beng.R;
import com.beng.utils.Tools;
import com.beng.utils.UIUtils;


/**
 * 模态对话框
 *
 * @author wjy
 */
public class MyProgressBar extends Dialog {
    private Activity context;
    private TextView tv_content;
    private ProgressBar mProgressBar;
    private View view;

    /**
     * 构造参数
     *
     * @param context 上下文
     */
    public MyProgressBar(Context context) {
        super(context, R.style.dialog_no_animation);
        this.context = (Activity) context;
        view = LayoutInflater.from(context).inflate(R.layout.lib_progress_bar,
                null);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public void show() {
        mProgressBar.setVisibility(View.VISIBLE);
        tv_content.setText(R.string.dialog_loading_data);
        super.show();
    }

    public void show(int resID) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (tv_content != null) {
            tv_content.setText(UIUtils.getString(resID));
        }
        super.show();
    }

    public void show(String msg) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (tv_content != null) {
            tv_content.setText(Tools.trim(msg));
        }
        super.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        context.finish();
    }

}
