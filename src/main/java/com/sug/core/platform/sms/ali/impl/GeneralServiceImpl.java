package com.sug.core.platform.sms.ali.impl;

import com.sug.core.platform.sms.ali.message.GeneralMessageForm;
import com.sug.core.platform.sms.ali.SmsService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Greg.chen on 2016-03-14.
 */
@Component
@Scope(value = "prototype")
public class GeneralServiceImpl<M> extends SmsService<M> {


    private static final String URL = "http://gw.api.taobao.com/router/rest";//环境调用地址

    @Override
    public void send(GeneralMessageForm form, M message) throws Exception {
        String content = super.builderContent(form, message);
        super.getResult(URL, content);
    }
}
