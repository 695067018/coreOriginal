package com.sug.core.platform.wechat.Certificate;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WeChatToken {
    private String accessToken;

    private Date generateTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }
}
