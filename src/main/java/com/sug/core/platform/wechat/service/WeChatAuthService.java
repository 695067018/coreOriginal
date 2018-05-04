package com.sug.core.platform.wechat.service;

import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.entity.WeChatOAuthEntity;
import com.sug.core.platform.wechat.response.WeChatOAuthTokenResponse;
import com.sug.core.platform.wechat.response.WeChatUserInfoResponse;
import com.sug.core.platform.wx.service.WxPayService;
import com.sug.core.rest.client.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;

import static com.sug.core.platform.wechat.constants.WeChatParams.MP_TYPE;
import static com.sug.core.platform.wechat.constants.WeChatUrlConstants.*;

@Service
public class WeChatAuthService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatAuthService.class);

    @Autowired
    private WeChatParams params;

    private static final Logger log = LoggerFactory.getLogger(WeChatAuthService.class);

    private static final Integer ACCESS_TOKEN_EXPIRED = 3600000;

    /**
     * @param callbackUrl wechat service callback url with code
     * @param redirectUrl redirectUrl after get access_token and openId with code
     */
    public String getInfoAuthUrl(String callbackUrl,String redirectUrl) {
        try {
            return String.format(INFO_AUTH_URL,params.getMpAppId(),
                    URLEncoder.encode(callbackUrl,"UTF-8"),
                    URLEncoder.encode(redirectUrl,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getBaseAuthUrl(String callbackUrl,String redirectUrl) {
        try {
            return String.format(BASE_AUTH_URL,params.getMpAppId(),
                    URLEncoder.encode(callbackUrl,"UTF-8"),
                    URLEncoder.encode(redirectUrl,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public WeChatOAuthEntity getOAuth(String code,int type) throws Exception {
        String uri = String.format(GET_OAUTH_URL,type == MP_TYPE ? params.getMpAppId() : params.getOpenAppId(),
                type == MP_TYPE ? params.getMpAppSecret() : params.getOpenAppSecret(),code);

        WeChatOAuthTokenResponse response = SimpleHttpClient.get(uri,WeChatOAuthTokenResponse.class);

        if(!Objects.isNull(response.getErrcode()) && !"0".equalsIgnoreCase(response.getErrcode())){
            logger.error("weChat get oauth access_token fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
            throw new RuntimeException("weChat get oauth access_token fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
        }

        WeChatOAuthEntity entity = new WeChatOAuthEntity();

        BeanUtils.copyProperties(response,entity);
        entity.setAccessTokenTime(new Date());

        return entity;
    }

    public WeChatUserInfoResponse getUserInfo(WeChatOAuthEntity entity) throws Exception {
        String accessToken = entity.getAccess_token();

        if(entity.getAccessTokenTime().getTime() + ACCESS_TOKEN_EXPIRED < System.currentTimeMillis()){
            accessToken = refreshToken(entity.getRefresh_token());
        }

        String uri = String.format(GET_INFO_URL,accessToken,entity.getOpenid());

        WeChatUserInfoResponse response = SimpleHttpClient.get(uri,WeChatUserInfoResponse.class);

        if(!Objects.isNull(response.getErrcode()) && !"0".equalsIgnoreCase(response.getErrcode())){
            throw new RuntimeException("weChat get userInfo fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
        }

        return response;
    }

    private String refreshToken(String refreshToken) throws Exception {
        String uri = String.format(REFRESH_TOKEN_URL,params.getMpAppId(),refreshToken);

        WeChatOAuthTokenResponse response = SimpleHttpClient.get(uri,WeChatOAuthTokenResponse.class);

        if(!Objects.isNull(response.getErrcode()) && !"0".equalsIgnoreCase(response.getErrcode())){
            throw new RuntimeException("weChat refresh fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
        }

        return response.getAccess_token();
    }
}
