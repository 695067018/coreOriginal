package com.fow.core.platform.wx.service;

import com.fow.core.rest.client.SimpleHttpClient;
import com.fow.core.util.StringUtils;
import com.fow.core.platform.wx.model.WxConfig;
import com.fow.core.platform.wx.model.WxToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Greg.Chen on 2015/6/4.
 */
@Service
public class WxTokenService {

    private static final String grant_type = "client_credential";
    private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private final int EXPIRES_IN = 3600;

    @Autowired
    private WxConfig wxConfig;


    public String getToken() throws Exception {
        WxToken token = WxToken.getInstance();

        String uri = GET_TOKEN_URL + "?grant_type=" + grant_type + "&appid=" + wxConfig.getAppId() + "&secret=" + wxConfig.getAppSecret();


        if (token == null || !StringUtils.hasText(token.getAccess_token())
                || token.getUpdateTime().getTime() + EXPIRES_IN < System.currentTimeMillis()) {
            token = SimpleHttpClient.get(uri, WxToken.class);
            token.setUpdateTime(new Date());
        }

        return token.getAccess_token();
    }

}
