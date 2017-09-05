package com.sug.core.platform.sms.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.sug.core.platform.json.JSONBinder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Greg.chen on 2016-03-14.
 */
@Component
@Scope(value = "prototype")
public abstract class SmsService{

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private static final ReentrantLock lock = new ReentrantLock();

    @Value("${aliyun.sms.appkey}")
    private String appkey;
    @Value("${aliyun.sms.secret}")
    private String secret;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    protected <M> void send(String phone,String signName,String templateCode,M params) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendSmsRequest request = new SingleSendSmsRequest();
        
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setRecNum(phone);
        request.setParamString(JSONBinder.binder((Class<M>) params.getClass()).toJSON(params));

        SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
    }

    protected void send(String phone, String signName, String templateCode, Map<String,Object> params) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendSmsRequest request = new SingleSendSmsRequest();

        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setRecNum(phone);
        request.setParamString(new JSONObject(params).toString());

        SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
    }
}
