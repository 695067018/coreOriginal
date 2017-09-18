package com.sug.core.platform.sms.aliyun.response;

import com.aliyuncs.transform.UnmarshallerContext;

public class SendSmsResponseUnmarshaller {
    public static SendSmsResponse unmarshall(SendSmsResponse sendSmsResponse, UnmarshallerContext context) {

        sendSmsResponse.setRequestId(context.stringValue("SendSmsResponse.RequestId"));
        sendSmsResponse.setBizId(context.stringValue("SendSmsResponse.BizId"));
        sendSmsResponse.setCode(context.stringValue("SendSmsResponse.Code"));
        sendSmsResponse.setMessage(context.stringValue("SendSmsResponse.Message"));

        return sendSmsResponse;
    }
}
