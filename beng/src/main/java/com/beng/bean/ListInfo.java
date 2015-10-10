package com.beng.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Jomelu on 15/9/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListInfo implements Serializable {

    private long gameNO;
    private String winNum;
    private long  insertG;
    private long  winG;

    public long getInsertG() {
        return insertG;
    }

    public void setInsertG(long insertG) {
        this.insertG = insertG;
    }

    public long getWinG() {
        return winG;
    }

    public void setWinG(long winG) {
        this.winG = winG;
    }

    public long getGameNO() {
        return gameNO;
    }

    public void setGameNO(long gameNO) {
        this.gameNO = gameNO;
    }

    public String getWinNum() {
        return winNum;
    }

    public void setWinNum(String winNum) {
        this.winNum = winNum;
    }

}
