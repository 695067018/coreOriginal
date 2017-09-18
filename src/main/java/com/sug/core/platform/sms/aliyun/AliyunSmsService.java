package com.sug.core.platform.sms.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.sug.core.platform.json.JSONBinder;
import com.sug.core.platform.sms.aliyun.request.SendSmsRequest;
import com.sug.core.platform.sms.aliyun.response.SendSmsResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Greg.chen on 2016-03-14.
 */
@Service
public class AliyunSmsService{

    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsService.class);

    @Value("${aliyun.appId}")
    private String appkey;
    @Value("${aliyun.appKey}")
    private String secret;

    public <T> void send(String phone,String signName,String templateCode,T params) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi",  "dysmsapi.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setPhoneNumbers(phone);
        request.setTemplateParam(JSONBinder.binder((Class<T>) params.getClass()).toJSON(params));

        SendSmsResponse httpResponse = client.getAcsResponse(request);
        if(!httpResponse.getCode().equalsIgnoreCase("OK")){
            logger.error("aliyun send sms fail : " + httpResponse.getCode() + ",msg : " + httpResponse.getMessage());
        }
    }

    public void send(String phone, String signName, String templateCode, Map<String,Object> params) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();

        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setPhoneNumbers(phone);
        request.setTemplateParam(new JSONObject(params).toString());

        SendSmsResponse httpResponse = client.getAcsResponse(request);
        if(!httpResponse.getCode().equalsIgnoreCase("OK")){
            logger.error("aliyun send sms fail : " + httpResponse.getCode() + ",msg : " + httpResponse.getMessage());
        }
    }
}
