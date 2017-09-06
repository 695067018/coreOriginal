package com.sug.core.platform.dingtalk.domain;

import java.util.Date;

public class DingtalkToken {
    private String token;
    private Date generateTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }
}
