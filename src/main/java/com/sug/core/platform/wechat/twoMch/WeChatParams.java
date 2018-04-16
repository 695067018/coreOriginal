package com.sug.core.platform.wechat.twoMch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("twoMchWeChatParams")
public class WeChatParams {
    @Value("${weChat.mp.appId:@null}")
    private String mpAppId;

    @Value("${weChat.open.appId:@null}")
    private String openAppId;

    @Value("${weChat.mp.appSecret:@null}")
    private String mpAppSecret;

    @Value("${weChat.mp.mchId:@null}")
    private String mpMchId;

    @Value("${weChat.mp.pay.apiSecret:@null}")
    private String mpApiSecret;

    @Value("${weChat.open.mchId:@null}")
    private String openMchId;

    @Value("${weChat.open.pay.apiSecret:@null}")
    private String openApiSecret;

    @Value("${weChat.pay.notifyUrl:@null}")
    private String notifyUrl;

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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getMpMchId() {
        return mpMchId;
    }

    public String getMpApiSecret() {
        return mpApiSecret;
    }

    public String getOpenMchId() {
        return openMchId;
    }

    public String getOpenApiSecret() {
        return openApiSecret;
    }

    public String getIp() {
        return ip;
    }
}
