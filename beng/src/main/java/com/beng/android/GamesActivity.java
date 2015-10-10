package com.beng.android;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beng.Constants;
import com.beng.MyApplication;
import com.beng.R;
import com.beng.base.BaseActivity;
import com.beng.bean.BaseResponse;
import com.beng.bean.ListInfo;
import com.beng.bean.ListInfoReq;
import com.beng.bean.Login;
import com.beng.http.ParamManage;
import com.beng.http.Request;
import com.beng.http.ResponseListener;
import com.beng.http.UrlManage;
import com.beng.iterface.IGameMethor;
import com.beng.methor.RadioFanBeiM;
import com.beng.utils.CommonUtil;
import com.beng.utils.PreferenceManager;
import com.beng.utils.TBArrayUtils;
import com.beng.utils.ToastUtil;
import com.sohu.focus.framework.loader.FocusResponseError;
import com.sohu.focus.framework.loader.LoaderInterface;
import com.sohu.focus.framework.util.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by Jomelu on 15/9/30.
 */
public class GamesActivity extends BaseActivity {
    private TextView time;
    private TextView profit;
    private TextView campital;
    private MyCount mc;
    private String sessioID;
    private long uid;
    private String pd;
    private long baseUserDou;
    private boolean isFristLoading = true;
    private IGameMethor mIGameMethor;

    private Button btnSeting;
    private Button starting;


    private final static int STOPMIN = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case STOPMIN:
                    getInfo();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);
        sessioID = PreferenceManager.getInstance(mContext).getStringData(Constants.PRE_SESSIONID, "");
        uid = PreferenceManager.getInstance(mContext).getLongData(Constants.PRE_UID, 0);
        pd = PreferenceManager.getInstance(mContext).getStringData(Constants.PRE_PD, "");
        mIGameMethor = new RadioFanBeiM(sessioID, uid);

        initView();

    }

    private void initView() {
        time = (TextView) findViewById(R.id.time);
        profit = (TextView) findViewById(R.id.profit);
        campital = (TextView) findViewById(R.id.capital);

        btnSeting = (Button) findViewById(R.id.setting);
        starting = (Button) findViewById(R.id.start);
        btnSeting.setOnClickListener(this);
        starting.setOnClickListener(this);

    }


    /**
     * 获取信息
     */
    private void getInfo() {
        String cookies = createCookies();
        mProgressBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("Cookie2", "$Version=1");
        params.put("Host", "cellapi.bengbeng.com");
        new Request<ListInfoReq>(this).url(UrlManage.list).addHeaders(params).cache(false).clazz(ListInfoReq.class).method(LoaderInterface.RequestMethod.POST)
                .setCookies(cookies).listener(new ResponseListener<ListInfoReq>() {

            @Override
            public void loadFinish(ListInfoReq response, long dataTime) {

                mProgressBar.dismiss();
                if (response != null) {
                    if (response.getError() == 1) {
                        mIGameMethor.dealInfo(response);
                        long time = response.getcDate() * 1000;
                        mc = new MyCount(time, 1000);
                        mc.start();

                        if (isFristLoading) {
                            if (!TextUtils.isEmpty(response.getUserDou()) && TextUtils.isDigitsOnly(response.getUserDou()))
                                baseUserDou = Long.valueOf(response.getUserDou());
                            else
                                baseUserDou = 0;
                            isFristLoading = false;
                        }

                        showResult(response);


                    } else {


                    }
                } else {
                    ToastUtil.toast("失败");

                }

            }

            @Override
            public void loadFromCache(ListInfoReq cacheResponse, long dataTime) {

            }

            @Override
            public void loadError(FocusResponseError.CODE errorCode) {

                ToastUtil.toast("失败");
                mProgressBar.dismiss();

            }

            @Override
            public void loadNativeReqHeader(Map<String, String> headers) {

            }
        }).exec();


    }


    private String createCookies() {
//bengLoginID=8216007_94f2d9c3c20694059ea932f4766f632a; userHit=true; cGlobal[authID]=8216007; cGlobal[authTime]=1443571750; PHPSESSID=ahi5173l80irbn03aqfmhmdpfv5kdf4p

        String bengLoginID = uid + "";
        String timeStr = MyApplication.getInstance().authorTime;
        ToastUtil.toast(timeStr + "");
        String cookes = MyApplication.getInstance().bengLoginId + "; " + MyApplication.getInstance().userHit + "; " + MyApplication.getInstance().authorID + "; " + timeStr + "; " + MyApplication.getInstance().phpSessid;
        ToastUtil.toast(cookes);
        return cookes;

    }


    private void showResult(ListInfoReq mListInfoReq) {
        if (mListInfoReq == null)
            return;

        long currentUserDou;
        if (!TextUtils.isEmpty(mListInfoReq.getUserDou()) && TextUtils.isDigitsOnly(mListInfoReq.getUserDou()))
            currentUserDou = Long.valueOf(mListInfoReq.getUserDou());

        profit.setText("当前豆豆：" + mListInfoReq.getUserDou());
        campital.setText("盈利：" + TBArrayUtils.getInstance().getWinResult());

    }


    /**
     * 倒计时
     */

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            time.setText("正在等待开奖！");
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = STOPMIN;
                    mHandler.sendMessage(message);
                }
            }).start();


        }


        @Override
        public void onTick(long millisUntilFinished) {
            time.setText("离投注截止时间：(" + millisUntilFinished / 1000 + ")...");
            mIGameMethor.postData(millisUntilFinished);

        }


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.setting:
                goToOthers(SettingActivity.class);

                break;
            case R.id.start:
                getInfo();

                break;
        }
    }
}
