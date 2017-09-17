package com.sug.core.platform.wechat.constants;

public class WeChatUrlConstants {
    //oauth snsapi_base
    public static final String BASE_AUTH_URL =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
    //oauth snsapi_info
    public static final String INFO_AUTH_URL =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
    //oauth access_token openid
    public static final String GET_OAUTH_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    //oauth refresh access_token
    public static final String REFRESH_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    //oauth user info
    public static final String GET_INFO_URL =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public static final String MSG_PUSH_URL =
            "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
}
