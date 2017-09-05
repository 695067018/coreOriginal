package com.sug.core.platform.wx.model;

/**
 * Created by Administrator on 2015/9/7.
 */
public class WxPayOauthResp {

    private String access_token;
    private Integer expires_in;
    private String openid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "WxPayOauthResp{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", openid='" + openid + '\'' +
                '}';
    }
}
