package com.sug.core.platform.dingtalk.service;

import com.sug.core.platform.dingtalk.domain.DingtalkToken;
import com.sug.core.platform.dingtalk.domain.SendMsgText;
import com.sug.core.platform.dingtalk.request.DingtalkCreateChatForm;
import com.sug.core.platform.dingtalk.request.DingtalkSendMsgForm;
import com.sug.core.platform.dingtalk.response.DingtalkCommonResponse;
import com.sug.core.platform.dingtalk.response.DingtalkCreateChatResponse;
import com.sug.core.platform.dingtalk.response.DingtalkTokenResponse;
import com.sug.core.platform.dingtalk.response.DingtalkUserIdResponse;
import com.sug.core.rest.client.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class DingtalkCommonService {
    private static final Logger logger = LoggerFactory.getLogger(DingtalkCommonService.class);

    private static final String CREATE_CHAT_URL = "https://oapi.dingtalk.com/chat/create";

    private static final String SEND_CHAT_URL = "https://oapi.dingtalk.com/chat/send";

    private static final String GET_USERID_URL = "https://oapi.dingtalk.com/user/get_by_mobile";

    @Autowired
    private DingtalkTokenService tokenService;

    public String getUserIdByMobile(String phone) throws Exception {
        String uri = GET_USERID_URL + "?access_token=" + tokenService.getToken() + "&mobile=" + phone;

        DingtalkUserIdResponse response = SimpleHttpClient.get(uri, DingtalkUserIdResponse.class);

        if(response.getErrcode().equalsIgnoreCase("0")){
            return response.getUserid();
        }

        throw new RuntimeException("dingtalk get user id fail, errcode :"
                + response.getErrcode() + ", errmsg :" + response.getErrmsg());
    }

    public String createConversationByPhone(String chatName,String phone) throws Exception {
        String uri = CREATE_CHAT_URL + "?access_token=" + tokenService.getToken();

        DingtalkCreateChatForm form = new DingtalkCreateChatForm();
        form.setName(chatName);

        String userId = getUserIdByMobile(phone);

        form.setOwner(userId);
        form.setUseridlist(Collections.singletonList(userId));

        DingtalkCreateChatResponse response = SimpleHttpClient.post(uri,DingtalkCreateChatResponse.class,form);

        if(response.getErrcode().equalsIgnoreCase("0")){
            return response.getChatid();
        }

        throw new RuntimeException("dingtalk create chat fail, errcode :"
                + response.getErrcode() + ", errmsg :" + response.getErrmsg());
    }

    public void sendMsg(String chatId,String text) {
        try {
            String uri = SEND_CHAT_URL + "?access_token=" + tokenService.getToken();

            SendMsgText msg = new SendMsgText();
            msg.setContent(text);

            DingtalkSendMsgForm form = new DingtalkSendMsgForm();
            form.setChatid(chatId);
            form.setMsgtype("text");
            form.setText(msg);

            DingtalkCommonResponse response = SimpleHttpClient.post(uri, DingtalkCommonResponse.class, form);

            if (!response.getErrcode().equalsIgnoreCase("0")) {
                throw new RuntimeException("dingtalk send Msg fail, errcode :"
                        + response.getErrcode() + ", errmsg :" + response.getErrmsg());
            }
        } catch (Exception ex){
            logger.error("dingtalk send msg fail:" + ex.getMessage());
        }
    }
}
