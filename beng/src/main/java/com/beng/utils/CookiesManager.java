package com.beng.utils;

/**
 * Created by Jomelu on 15/10/10.
 */
public class CookiesManager {
    private static CookiesManager ourInstance = new CookiesManager();

    public static CookiesManager getInstance() {
        return ourInstance;
    }

    private CookiesManager() {
    }



}
