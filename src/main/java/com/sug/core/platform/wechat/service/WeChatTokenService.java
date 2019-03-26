package com.sug.core.platform.wechat.service;

import com.sug.core.platform.wechat.Certificate.WeChatToken;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.response.WeChatTokenResponse;
import com.sug.core.platform.wx.model.WxToken;
import com.sug.core.rest.client.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Service
public class WeChatTokenService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatTokenService.class);

    private static final String GET_TOKEN_URL
            = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static final Integer EXPIRES_IN = 3600 * 1000;

    @Autowired
    private WeChatParams params;

    @Autowired
    private WeChatToken token;

    public String getToken() throws Exception {

        String uri = String.format(GET_TOKEN_URL, params.getMpAppId(), params.getMpAppSecret());

        if (!StringUtils.hasText(token.getAccessToken())
                || token.getGenerateTime().getTime() + EXPIRES_IN < System.currentTimeMillis()) {
            synchronized (this) {
                WeChatTokenResponse response = SimpleHttpClient.get(uri, WeChatTokenResponse.class);
                if (!Objects.isNull(response.getErrcode())) {
                    logger.error("weChat access_token fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
                }
                token.setAccessToken(response.getAccess_token());
                token.setGenerateTime(new Date());
            }
        }

        return token.getAccessToken();
    }
}
