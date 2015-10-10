package com.beng;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.beng.android.GamesActivity;
import com.beng.base.BaseActivity;
import com.beng.bean.Login;
import com.beng.http.ParamManage;
import com.beng.http.Request;
import com.beng.http.ResponseListener;
import com.beng.http.UrlManage;
import com.beng.utils.CommonUtil;
import com.beng.utils.PreferenceManager;
import com.beng.utils.ToastUtil;
import com.beng.view.EmailAutoCompleteTextView;
import com.sohu.focus.framework.loader.FocusResponseError;
import com.sohu.focus.framework.loader.LoaderInterface;
import com.sohu.focus.framework.util.LogUtils;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity {

    private EmailAutoCompleteTextView name;
    private EditText pw;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        name = (EmailAutoCompleteTextView) findViewById(R.id.info_email);
        pw = (EditText) findViewById(R.id.pw);
        ok = (Button) findViewById(R.id.login);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postService();

            }

        });

    }

    private void postService() {
        mProgressBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("Cookie2", "version1");
        new Request<Login>(this).url(UrlManage.login).addHeaders(params).cache(false).clazz(Login.class).method(LoaderInterface.RequestMethod.POST)
                .postContent(ParamManage.postLogin(CommonUtil.getImei(this), pw.getText().toString()
                        , name.getText().toString(), getString(R.string.app_version))).listener(new ResponseListener<Login>() {
            @Override
            public void loadFinish(Login response, long dataTime) {
                mProgressBar.dismiss();

                if (response != null) {

                    if (response.getError() == 1) {
                        PreferenceManager.getInstance(mContext).saveData(Constants.PRE_SESSIONID, response.getSessionid());
                        PreferenceManager.getInstance(mContext).saveData(Constants.PRE_UID, response.getUserID());
                        PreferenceManager.getInstance(mContext).saveData(Constants.PRE_PD, response.getPd());
                        ToastUtil.toast("登陆成功");
                        goToOthers(GamesActivity.class);
                        mContext.finish();


                    } else {

                        ToastUtil.toast(response.getMsg());

                    }
                } else {
                    ToastUtil.toast("失败");

                }

            }

            @Override
            public void loadFromCache(Login cacheResponse, long dataTime) {

            }

            @Override
            public void loadError(FocusResponseError.CODE errorCode) {
                mProgressBar.dismiss();
                ToastUtil.toast("登陆失败");

            }

            @Override
            public void loadNativeReqHeader(Map<String, String> headers) {

                for (String key : headers.keySet()) {
                    LogUtils.i("key= " + key + " and value= " + headers.get(key));

                }

                String autoTimeStr = headers.get("Set-Cookie7");
                String phpSessIDStr = headers.get("Set-Cookie0");
                String autoAuthIdStr = headers.get("Set-Cookie6");
                String userHitStr = headers.get("Set-Cookie5");
                String bengIDStr = headers.get("Set-Cookie4");

                if (autoTimeStr != null && autoTimeStr.contains(";")) {
                    String str = autoTimeStr.split(";")[0];
                    MyApplication.getInstance().setAuthorTime(str);
                }
                if (phpSessIDStr != null && phpSessIDStr.contains(";")) {
                    String str = phpSessIDStr.split(";")[0];
                    MyApplication.getInstance().setPhpSessid(str);
                }
                if (autoAuthIdStr != null && autoAuthIdStr.contains(";")) {
                    String str = autoAuthIdStr.split(";")[0];
                    MyApplication.getInstance().setAuthorID(str);
                }
                if (userHitStr != null) {
                    MyApplication.getInstance().setUserHit(userHitStr);
                }
                if (bengIDStr != null && bengIDStr.contains(";")) {
                    String str = bengIDStr.split(";")[0];
                    MyApplication.getInstance().setBengLoginId(str);
                }


            }

        }).exec();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
