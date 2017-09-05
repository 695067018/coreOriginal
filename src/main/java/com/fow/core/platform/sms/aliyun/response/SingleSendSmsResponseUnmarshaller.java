package com.fow.core.platform.sms.aliyun.response;

import com.aliyuncs.transform.UnmarshallerContext;

/**
 * Created by user on 2016-07-25.
 */
public class SingleSendSmsResponseUnmarshaller {
    public SingleSendSmsResponseUnmarshaller() {
    }

    public static SingleSendSmsResponse unmarshall(SingleSendSmsResponse singleSendSmsResponse, UnmarshallerContext context) {
        singleSendSmsResponse.setRequestId(context.stringValue("SingleSendSmsResponse.RequestId"));
        return singleSendSmsResponse;
    }
}
