package com.sug.core.platform.wechat.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class WeChatParams {
    @Value("${weChat.appId:@null}")
    private String appId;

    @Value("${weChat.appSecret:@null}")
    private String appSecret;

    @Value("${weChat.mchId:@null}")
    private String mchId;

    @Value("${weChat.pay.notifyUrl:@null}")
    private String notifyUrl;

    @Value("${weChat.pay.apiSecret:@null}")
    private String apiSecret;

    @Value("${weChat.native.site.ip:@null}")
    private String ip;

    public static final String WECHAT_BROWSER = "MicroMessenger";

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getMchId() {
        return mchId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getIp() {
        return ip;
    }
}
