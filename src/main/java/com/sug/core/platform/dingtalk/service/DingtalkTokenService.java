package com.sug.core.platform.dingtalk.service;

import com.sug.core.platform.dingtalk.domain.DingtalkToken;
import com.sug.core.platform.dingtalk.response.DingtalkTokenResponse;
import com.sug.core.platform.wx.model.WxToken;
import com.sug.core.rest.client.SimpleHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Random;


@Service
public class DingtalkTokenService {

    private static final String GET_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    private static final int EXPIRES_IN = 3600 * 1000;

    private DingtalkToken tokenCopy = new DingtalkToken();

    @Value("${dingtalk.corpId:@null}")
    private String corpId;

    @Value("${dingtalk.corpSecret:@null}")
    private String corpSecret;

    public String getToken() throws Exception {

        String uri = GET_TOKEN_URL + "?corpid=" + corpId + "&corpsecret=" + corpSecret;

        synchronized (this) {
            if (StringUtils.isEmpty(tokenCopy.getToken())
                    || tokenCopy.getGenerateTime().getTime() + EXPIRES_IN < System.currentTimeMillis()) {
                DingtalkTokenResponse response = SimpleHttpClient.get(uri, DingtalkTokenResponse.class);
                if(response.getErrcode().equalsIgnoreCase("0") ){
                    tokenCopy.setToken(response.getAccess_token());
                    tokenCopy.setGenerateTime(new Date());
                }else {
                    throw new RuntimeException("get dingtalk access_token fail, errcode :"
                            + response.getErrcode() + ", errmsg : " + response.getErrmsg());
                }
            }
        }

        return tokenCopy.getToken();
    }

}
