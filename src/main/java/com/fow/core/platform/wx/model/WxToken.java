package com.fow.core.platform.wx.model;

import java.util.Date;

/**
 * Created by Greg.Chen on 2015/6/4.
 */
public class WxToken {

    private static final WxToken token = new WxToken();
    private WxToken(){
        updateTime = new Date();
    }

    public static WxToken getInstance(){
        return token;
    }


    private String access_token;

    private Date updateTime;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
