package com.sug.core.platform.wechat.service;

import com.sug.core.platform.crypto.SHA1;
import com.sug.core.platform.wechat.Certificate.WeChatJsTicket;
import com.sug.core.platform.wechat.Certificate.WeChatToken;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.response.WeChatJsConfigResponse;
import com.sug.core.platform.wechat.response.WeChatJsPayResponse;
import com.sug.core.platform.wechat.response.WeChatJsTicketResponse;
import com.sug.core.platform.wechat.response.WeChatTokenResponse;
import com.sug.core.platform.wx.service.MD5Util;
import com.sug.core.rest.client.SimpleHttpClient;
import com.sug.core.util.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class WeChatJsParamsService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatJsParamsService.class);

    private static final String GET_TOKEN_URL
            = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    private static final Integer EXPIRES_IN = 3600 * 1000;

    @Autowired
    private WeChatTokenService tokenService;

    @Autowired
    private WeChatParams params;

    private WeChatJsTicket ticket = new WeChatJsTicket();

    private String getTicket() throws Exception {

        String uri = String.format(GET_TOKEN_URL, tokenService.getToken());

        synchronized (this) {
            if (!StringUtils.hasText(ticket.getJsTicket())
                    || ticket.getGenerateTime().getTime() + EXPIRES_IN < System.currentTimeMillis()) {
                WeChatJsTicketResponse response = SimpleHttpClient.get(uri, WeChatJsTicketResponse.class);
                if (Objects.nonNull(response.getErrcode()) &&  !"0".equals(response.getErrcode())) {
                    logger.error("weChat jsTicket fail,errCode:" + response.getErrcode() + ",errMsg:" + response.getErrmsg());
                }
                ticket.setJsTicket(response.getTicket());
                ticket.setGenerateTime(new Date());
            }
        }
        return ticket.getJsTicket();
    }

    public WeChatJsConfigResponse getJsConfigParams(String url,List<String> jsApiList) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);

        Long timestamp = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=");
        sb.append(this.getTicket());
        sb.append("&noncestr=");
        sb.append(nonce_str);
        sb.append("&timestamp=");
        sb.append(timestamp.toString());
        sb.append("&url=");
        sb.append(url);

        String signature = SHA1.encrypt(sb.toString());

        WeChatJsConfigResponse response = new WeChatJsConfigResponse();
        response.setAppId(params.getAppId());
        response.setNonceStr(nonce_str);
        response.setTimestamp(timestamp);
        response.setJsApiList(jsApiList);
        response.setSignature(signature);

        return response;
    }
}
