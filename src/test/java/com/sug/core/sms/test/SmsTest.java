package com.sug.core.sms.test;

import com.sug.core.platform.sms.ali.impl.GeneralServiceImpl;
import com.sug.core.platform.sms.ali.message.GeneralMessageForm;
import com.sug.core.platform.sms.ali.SmsService;
import com.sug.core.platform.sms.ali.message.CaptchaMessage;
import org.junit.Test;

/**
 * Created by Greg.chen on 2016-03-14.
 */
public class SmsTest {

    @Test
    public void getTest() throws Exception {
        SmsService<CaptchaMessage> smsService = new GeneralServiceImpl();

        smsService.setAppkey("233211661111");
        smsService.setSecret("af3c5ea5f7386ffc76a9d472a053b221");

        GeneralMessageForm form = new GeneralMessageForm("注册验证","SMS_5255019","18006003050");
        CaptchaMessage msg = new CaptchaMessage(form.getPhone());

        smsService.send(form, msg);

    }
}
