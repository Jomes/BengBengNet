package com.beng.utils;

import com.sohu.focus.framework.util.LogUtils;

import java.util.Random;

/**
 * Created by Jomelu on 15/9/30.
 */
public class TBArrayUtils {
    private static TBArrayUtils ourInstance = new TBArrayUtils();
    public int baseBeishu;
    public int initBeishu = 1;//初始化倍数
    private long winResult = 0;//盈利

    public long getWinResult() {
        return winResult;
    }

    public void setWinResult(long winResult) {
        this.winResult = winResult;
    }

    public int getBaseBeishu() {
        return baseBeishu;
    }

    public void setBaseBeishu(int baseBeishu) {
        this.baseBeishu = baseBeishu;
    }

    public int getInitBeishu() {
        return initBeishu;
    }

    public void setInitBeishu(int initBeishu) {
        this.initBeishu = initBeishu;
    }


    public static TBArrayUtils getInstance() {
        return ourInstance;
    }

    private TBArrayUtils() {
    }

    public static final int[] xiazhu = new int[]{1, 3, 6, 10, 15, 21, 28,
            36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15,
            10, 6, 3, 1};


    public int getRadom(int max, int min) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }
}
