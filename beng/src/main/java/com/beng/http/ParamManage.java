package com.beng.http;

import android.content.Context;

import com.beng.utils.BaseParamUtils;
import com.beng.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @author jomeslu
 */
public class ParamManage {


    /**
     * 登陆
     *
     * @return
     */
    public static String postLogin(String device, String tbUserPwd, String tbUserAccount, String version) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("device", device);
        map.put("isEmulator", "0");
        map.put("tbUserPwd", tbUserPwd);
        map.put("version", version);
        map.put("tbUserAccount", tbUserAccount);
        return BaseParamUtils.parase(map);
    }


    public static String postInfoList(String act,String game){
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", act);
        map.put("game", game);
        return BaseParamUtils.parase(map);

    }

    public static String postInsert(String sessionid,String tbNum,String gameNO,String userID){
        Map<String, String> map = new HashMap<String, String>();
        map.put("sessionid", sessionid);
        map.put("tbNum", tbNum);
        map.put("gameNO", gameNO);
        map.put("userID", userID);
        return BaseParamUtils.parase(map);

    }


}
