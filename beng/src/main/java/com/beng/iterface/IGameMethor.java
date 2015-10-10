package com.beng.iterface;

import com.beng.bean.ListInfoReq;

/**
 * Created by Jomelu on 15/10/2.
 */
public interface IGameMethor {

    /**
     * 投注
     */
    void postData(long millisUntilFinished );

    /**
     * 返回的 信息 进行处理
     * @param response
     */
    void dealInfo(ListInfoReq response);



}
