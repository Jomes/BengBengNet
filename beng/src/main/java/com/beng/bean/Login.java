package com.beng.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jomelu on 15/9/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login extends BaseResponse {

    private String  pd;
    private String sessionid;
    private long userID;


    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
