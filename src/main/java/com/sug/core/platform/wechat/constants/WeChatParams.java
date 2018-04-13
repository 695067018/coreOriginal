package com.sug.core.platform.wechat.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class WeChatParams {
    @Value("${weChat.mp.appId:@null}")
    private String mpAppId;

    @Value("${weChat.open.appId:@null}")
    private String openAppId;

    @Value("${weChat.mp.appSecret:@null}")
    private String mpAppSecret;

    @Value("${weChat.mchId:@null}")
    private String mchId;

    @Value("${weChat.pay.notifyUrl:@null}")
    private String notifyUrl;

    @Value("${weChat.pay.apiSecret:@null}")
    private String apiSecret;

    @Value("${weChat.native.site.ip:@null}")
    private String ip;

    public static final String WECHAT_BROWSER = "MicroMessenger";

    public String getMpAppId() {
        return mpAppId;
    }

    public String getOpenAppId() {
        return openAppId;
    }

    public String getMpAppSecret() {
        return mpAppSecret;
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
