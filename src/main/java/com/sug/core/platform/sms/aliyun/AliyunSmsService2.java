package com.sug.core.platform.sms.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sug.core.platform.json.JSONBinder;
import com.sug.core.platform.sms.aliyun.request.SendSmsRequest;
import com.sug.core.platform.sms.aliyun.response.SendSmsResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AliyunSmsService2 {

    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsService2.class);

    @Value("${aliyun.sms.appId}")
    private String appkey;
    @Value("${aliyun.sms.appKey}")
    private String secret;
    @Value("${aliyun.sms.regionId}")
    private String regionId;

    public <T> void send(String phone,String signName,String templateCode,T params) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile(regionId, appkey, secret);
        DefaultProfile.addEndpoint(regionId, "Dysmsapi",  "dysmsapi.aliyuncs.com");
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
        IClientProfile profile = DefaultProfile.getProfile(regionId, appkey, secret);
        DefaultProfile.addEndpoint(regionId, "Sms",  "sms.aliyuncs.com");
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
