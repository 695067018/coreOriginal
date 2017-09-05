package com.sug.core.platform.sms.aliyun.request;

import com.aliyuncs.RpcAcsRequest;
import com.sug.core.platform.sms.aliyun.response.SingleSendSmsResponse;

/**
 * Created by user on 2016-07-25.
 */
public class SingleSendSmsRequest extends RpcAcsRequest<SingleSendSmsResponse> {

    private String signName;
    private String templateCode;
    private String recNum;
    private String paramString;

    public SingleSendSmsRequest() {
        super("Dm", "2015-11-23", "SingleSendSms");
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
        this.putQueryParameter("SignName", signName);
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        this.putQueryParameter("TemplateCode", templateCode);
    }

    public String getRecNum() {
        return recNum;
    }

    public void setRecNum(String recNum) {
        this.recNum = recNum;
        this.putQueryParameter("RecNum", recNum);
    }

    public String getParamString() {
        return paramString;
    }

    public void setParamString(String paramString) {
        this.paramString = paramString;
        this.putQueryParameter("ParamString", paramString);
    }

    public Class<SingleSendSmsResponse> getResponseClass() {
        return SingleSendSmsResponse.class;
    }
}
