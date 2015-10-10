package com.sohu.focus.framework.upgrade;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sohu.focus.focusframework.R;

/**
 * 更新提示框
 * 
 * @author jomeslu
 * 
 */
public class UpgradeDialog extends Dialog implements View.OnClickListener, LibEventCallBackListener {
	private Context mContext;
	private Button mCancel;
	private View mContentView;
	private ProgressBar mProgressBar;
	private Button mSubmit;
	private String upGradeTips;
	private TextView mTips;
	private String apkUrl;
	private int icon;
	/********* 成功返回值 **********/
	private static final int SUCCESCODE = 3;
	/********* 失败返回值 **********/
	private static final int FAILURECODE = 2;
	/********* 进度条 **********/
	private static final int PROGRASSCODE = 1;
	/******** 是否强制更新 *******/
	private boolean isForceUpGrade = false;

	/**
	 * 
	 * @param context 上下文
	 * @param upGradeTips 更新提示
	 * @param isForceUpGrade 是否是强制更新。isForceUpGrade==true？ 强制更新：不是强制更新
	 * @param apkUrl apk 下载地址
	 * @param icon 应用的icon.
	 */
	public UpgradeDialog(Context context, String upGradeTips, boolean isForceUpGrade,
			String apkUrl, int icon) {
		super(context);
		mContext = context;
		this.upGradeTips = upGradeTips;
		this.isForceUpGrade = isForceUpGrade;
		this.apkUrl = apkUrl;
		this.icon = icon;
		initView();
		setCancelable(!isForceUpGrade);
	}


	private void initView() {
		mContentView = LayoutInflater.from(mContext).inflate(R.layout.upgrade_dialog, null);
		mTips = (TextView) mContentView.findViewById(R.id.upgrade_content);
		mSubmit = (Button) mContentView.findViewById(R.id.upgrade_ok);
		mCancel = (Button) mContentView.findViewById(R.id.upgrade_cancel);
		mProgressBar = (ProgressBar) mContentView.findViewById(R.id.lib_pb);
		mCancel.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mTips.setText(upGradeTips);
		if (!isForceUpGrade) {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setGravity(Gravity.CENTER);
		window.setBackgroundDrawableResource(R.color.transparent);
		setContentView(mContentView);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LibEvent.getInstance(mContext).registEventManagerListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.upgrade_ok) {

			mSubmit.setClickable(false);
			new Upgrade.Builder(mContext).setUrl(apkUrl).serIconId(icon)
					.upgrade(new Upgrade.UpgradeDialogFinishListener() {
						@Override
						public void onUpgradeDialogFinish(boolean isUpgradeStarted) {


						}
					});

			if (isForceUpGrade)
				mProgressBar.setVisibility(View.VISIBLE);
			else
				dismiss();

		} else if (v.getId() == R.id.upgrade_cancel) {
			if (isForceUpGrade) {
				NotificationManager notiManager =
						(NotificationManager) mContext
								.getSystemService(Context.NOTIFICATION_SERVICE);
				notiManager.cancel(1001);
				System.exit(0);
			} else
				dismiss();
		}

	}

	@Override
	public void onEventResult(Object obj, int mode) {

		switch (mode) {
			case PROGRASSCODE:
				if (isForceUpGrade) {
					if (obj instanceof Integer) {
						Integer progressNum = (Integer) obj;
						if (mProgressBar != null) {
							mProgressBar.setProgress(progressNum);
						}
					}

				}

				break;
			case FAILURECODE:
				mSubmit.setClickable(true);

				break;
			case SUCCESCODE:
				mSubmit.setClickable(true);
				break;

			default:
				break;
		}

	}



}
