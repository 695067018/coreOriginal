package com.sug.core.platform.wechat.service;

import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.entity.WeChatOAuthEntity;
import com.sug.core.platform.wechat.request.WeChatPushMsgForm;
import com.sug.core.platform.wechat.response.WeChatOAuthTokenResponse;
import com.sug.core.platform.wechat.response.WeChatPushMsgResponse;
import com.sug.core.platform.wechat.response.WeChatUserInfoResponse;
import com.sug.core.rest.client.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.sug.core.platform.wechat.constants.WeChatUrlConstants.*;

@Service
public class WeChatMsgService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMsgService.class);

    @Autowired
    private WeChatTokenService weChatTokenService;

    public void sendMsg(WeChatPushMsgForm form) throws Exception {

        String accessToken = weChatTokenService.getToken();

        String url = String.format(MSG_PUSH_URL,accessToken);

        WeChatPushMsgResponse response = SimpleHttpClient.post(url,WeChatPushMsgResponse.class,form);

        if(!response.getErrcode().equals(0)){
            logger.error("wechat push msg fail:" + response.getErrcode() + ",msg:" + response.getErrmsg());
        }
    }
}
