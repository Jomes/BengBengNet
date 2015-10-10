package com.beng.methor;

import com.beng.MyApplication;
import com.beng.bean.BaseResponse;
import com.beng.bean.ListInfo;
import com.beng.bean.ListInfoReq;
import com.beng.http.ParamManage;
import com.beng.http.Request;
import com.beng.http.ResponseListener;
import com.beng.http.UrlManage;
import com.beng.iterface.IGameMethor;
import com.beng.utils.TBArrayUtils;
import com.beng.utils.ToastUtil;
import com.sohu.focus.framework.loader.FocusResponseError;
import com.sohu.focus.framework.loader.LoaderInterface;
import com.sohu.focus.framework.util.LogUtils;

import java.util.Map;

/**
 * Created by Jomelu on 15/10/4.
 */
public abstract class AbsMethor implements IGameMethor {


    protected void post(String seessionID, String tbnum, String usesrID, String gameId) {

        new Request<BaseResponse>(MyApplication.getInstance()).url(UrlManage.insert).cache(false).clazz(BaseResponse.class).method(LoaderInterface.RequestMethod.POST)
                .postContent(ParamManage.postInsert(seessionID, tbnum, gameId, usesrID)).listener(new ResponseListener<BaseResponse>() {
            @Override
            public void loadFinish(BaseResponse response, long dataTime) {

                if (response != null) {
                    if (response.getError() == 1) {
                        ToastUtil.toast(response.getMsg());

                    } else {

                    }
                } else {
                    ToastUtil.toast("失败");

                }

            }


            @Override
            public void loadFromCache(BaseResponse cacheResponse, long dataTime) {

            }

            @Override
            public void loadError(FocusResponseError.CODE errorCode) {

                ToastUtil.toast("失败");

            }

            @Override
            public void loadNativeReqHeader(Map<String, String> headers) {

            }
        }).exec();


    }


    @Override
    public void dealInfo(ListInfoReq mListInfoReq) {
        if (mListInfoReq == null)
            throw new NullPointerException("ListInfoReq is  null");

        if (mListInfoReq.getList() != null && mListInfoReq.getList().size() > 5) {

            ListInfo listInfo = mListInfoReq.getList().get(4);
            long winResult = listInfo.getInsertG() - listInfo.getWinG();
            TBArrayUtils.getInstance().setWinResult(TBArrayUtils.getInstance().getWinResult() + winResult);

            if (winResult > 0) {
                doWins();

            } else {
                dolose();
            }


        }


    }

    /**
     * 处理赢的
     */
    protected abstract void doWins();

    /**
     * 处理输的时候
     */
    protected abstract void dolose();


}
