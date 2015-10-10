package com.beng.methor;

import com.beng.bean.ListInfoReq;
import com.beng.utils.TBArrayUtils;
import com.sohu.focus.framework.util.LogUtils;

import java.util.Random;

/**
 * Created by Jomelu on 15/10/4.
 * <p/>
 * 方法1: 投单双是随机的，如果上了轮输了，那么在下一轮的基数＊3.
 * 投注方式：单双
 * 投注时间： 在投注截止前的28秒－5秒的随机数。
 */
public class RadioFanBeiM extends AbsMethor {

    public int currentPostRandio;//投注随机数
    private ListInfoReq infoReq;
    private String sessioID;
    private long uid;

    private RadioFanBeiM() {
    }


    public RadioFanBeiM(String sessioID, long uid) {
        this.sessioID = sessioID;
        this.uid = uid;


    }

    @Override
    public void postData(long millisUntilFinished) {

        if (millisUntilFinished == currentPostRandio) {
            post(sessioID, getRandiomPost(TBArrayUtils.getInstance().getBaseBeishu()), uid + "", infoReq.getGameNo() + "");

        }


    }

    @Override
    public void dealInfo(ListInfoReq mListInfoReq) {
        super.dealInfo(mListInfoReq);
        currentPostRandio = TBArrayUtils.getInstance().getRadom(8, 35);
    }

    @Override
    protected void doWins() {
        TBArrayUtils.getInstance().setBaseBeishu(TBArrayUtils.getInstance().initBeishu);

    }

    @Override
    protected void dolose() {
        int currentbeishu = TBArrayUtils.getInstance().getBaseBeishu();
        currentbeishu = currentbeishu * 3;
        TBArrayUtils.getInstance().setBaseBeishu(currentbeishu);

    }


    /**
     * 通过随机数来进行 进行投注
     *
     * @return
     */
    public String getRandiomPost(int beishu) {

        Random random = new Random(6);
        int i = random.nextInt();
        return getArr(i % 2 == 0, beishu);
    }

    private String getArr(boolean isEvent, int beishu) {
        String eventArr = ",";//偶数
        String radixArr = ",";//基数

        for (int i = 0; i < TBArrayUtils.xiazhu.length; i++) {

            if (i % 2 == 0) {
                radixArr = radixArr + "0,";
                eventArr = eventArr + TBArrayUtils.xiazhu[i] * beishu + ",";
            } else {
                radixArr = radixArr + TBArrayUtils.xiazhu[i] * beishu + ",";
                eventArr = eventArr + "0,";
            }


        }

        LogUtils.i("偶数：" + eventArr);
        LogUtils.i("基数：" + radixArr);

        return isEvent ? eventArr.substring(0, eventArr.length() - 1) : radixArr.substring(0, radixArr.length() - 1);
    }


}
