package com.beng.bean;

import java.util.ArrayList;

/**
 * Created by Jomelu on 15/9/30.
 */
public class ListInfoReq extends BaseResponse {

    private ArrayList<ListInfo> list;
    private long cDate;
    private long gameNo;
    private String userDou;

    public ArrayList<ListInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ListInfo> list) {
        this.list = list;
    }

    public long getcDate() {
        return cDate;
    }

    public void setcDate(long cDate) {
        this.cDate = cDate;
    }

    public long getGameNo() {
        return gameNo;
    }

    public void setGameNo(long gameNo) {
        this.gameNo = gameNo;
    }

    public String getUserDou() {
        return userDou;
    }

    public void setUserDou(String userDou) {
        this.userDou = userDou;
    }
}
