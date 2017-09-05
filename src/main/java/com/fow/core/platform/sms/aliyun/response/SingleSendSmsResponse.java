package com.fow.core.platform.sms.aliyun.response;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.transform.UnmarshallerContext;

/**
 * Created by user on 2016-07-25.
 */
public class SingleSendSmsResponse extends AcsResponse{
    private String requestId;

    public SingleSendSmsResponse() {
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public AcsResponse getInstance(UnmarshallerContext unmarshallerContext){
        return SingleSendSmsResponseUnmarshaller.unmarshall(this, unmarshallerContext);
    }
}
