package com.beng.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beng.R;
import com.beng.utils.CommonUtil;


/**
 * @author jomeslu
 */
public class CustomerDialog extends Dialog implements View.OnClickListener, OnItemClickListener {
    private Context mContext;
    private Button mCancel;
    private View mContentView;
    private ListView mListView;
    private View btnView;
    private TextView desc;
    private TextView areaTitle;
    private int dp;
    private String[] list=null;
    private BaseDailog mBaseDailog;


    public CustomerDialog(Context context, String[] list) {
        super(context);
        mContext = context;
        this.list = list;
        dp = CommonUtil.typeValue(context, 44);
        initView();
        mBaseDailog = new BaseDailog();
        mListView.setAdapter(mBaseDailog);
    }


    private void initView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null);
        mListView = (ListView) mContentView.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.FocusPopupAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContentView.getResources().getDisplayMetrics();
        setContentView(mContentView);
        int height = CommonUtil.getScreenHeight(mContext);
        getWindow().setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT );
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            default:
                break;
        }

    }


    private int currentPoisition = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String mBaseDialogMode = (String) parent.getItemAtPosition(position);
        if (mOnBtnClickListener != null) {
            mOnBtnClickListener.onbtnOk(mBaseDialogMode);
            currentPoisition = position;

        }
        dismiss();
        mBaseDailog.notifyDataSetChanged();

    }

    public void setCurrentPosition(int position) {
        currentPoisition = position;
    }

    public void setOnBtnClickListener(OnBtnClickListener onClickListener) {
        mOnBtnClickListener = onClickListener;
    }

    private OnBtnClickListener mOnBtnClickListener;

    public interface OnBtnClickListener {
        void onbtnOk(String mBaseDialogMode);
    }


    class BaseDailog extends BaseAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.length;
        }

        @Override
        public Object getItem(int position) {
            return list == null ? null : list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_dailog, null);
                holder = new ViewHolder();
                holder.areaName = (TextView) convertView.findViewById(R.id.cdetail_area_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp);
            convertView.setLayoutParams(params);
            holder.areaName.setText(list[position]);
            return convertView;
        }

        class ViewHolder {
            TextView areaName;
        }
    }


}
