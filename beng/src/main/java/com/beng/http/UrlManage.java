package com.beng.http;

import java.net.URL;


/**
 * @author jomeslu
 */
public class UrlManage {

    //正式环境
    private  static final String URL_BASE = "http://cellapi.bengbeng.com";
    //公用的
    public static final String login = URL_BASE+"/login.php";
    public static final String list = URL_BASE+"/game_api.php?act=index&game=28";
    public static final String insert = URL_BASE+"/game_api.php?act=insert&game=28";
}
