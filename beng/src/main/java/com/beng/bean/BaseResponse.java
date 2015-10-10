package com.beng.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 所有的服务器返回接口model继承自本类
 * @author lbritney
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = -2712952253473504988L;

    private String errorMessage;
    private String msg;
    
    private int type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
