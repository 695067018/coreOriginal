package com.fow.core.platform.leanCloud.service;

import com.fow.core.platform.leanCloud.LeanCloudClient;
import com.fow.core.platform.leanCloud.constants.InstallationType;
import com.fow.core.platform.leanCloud.request.push.BaseCustomParams;
import com.fow.core.platform.leanCloud.request.push.MessagePushForm;
import com.fow.core.platform.leanCloud.request.push.PushConditions;
import com.fow.core.platform.leanCloud.request.push.android.AndroidPushData;
import com.fow.core.platform.leanCloud.request.push.ios.IOSAlert;
import com.fow.core.platform.leanCloud.request.push.ios.IOSPushData;
import com.fow.core.platform.leanCloud.response.MessagePushResponse;
import com.fow.core.platform.web.rest.exception.InvalidRequestException;
import com.fow.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016-08-08.
 */
@Service
public class PushService {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PushService.class);


    private static final String URI = "https://leancloud.cn/1.1/push";

    @Autowired
    private LeanCloudClient leanCloudClient;

    @Autowired
    private MessagePushFormGenerator messagePushFormGenerator;

    @Value("${leanCloud.android.action}") //for android
    private String action;

    public String getAction() {
        return action;
    }

    public AndroidPushData simpleAndroidDataGenerator(String title, String content, BaseCustomParams params, boolean isAction){
        AndroidPushData data = new AndroidPushData();
        data.setTitle(title);
        data.setAlert(content);

        if(isAction){
            data.setAction(action);
            data.setPushCustomParams(params);
        }

        return data;
    }

    /**
     * Android推送
     * @param data  Alert内容
     * @param installationId 精确推送, 如果为空, 代表全局推送
     * @throws Exception
     */
    public void sendAndroid(AndroidPushData data, String installationId) throws Exception {
        PushConditions conditions = new PushConditions();
        conditions.setDeviceType(InstallationType.ANDROID.getCode());
        if(StringUtils.hasText(installationId)){
            conditions.setInstallationId(installationId);
        }
        this.push(messagePushFormGenerator.generateForm(data,conditions));
    }

    public IOSAlert simpleIOSAlertGenerator(String title, String content){
        IOSAlert iosAlert = new IOSAlert();
        iosAlert.setTitle(title);
        iosAlert.setBody(content);

        return iosAlert;
    }

    public IOSPushData simpleIOSDataGenerator(IOSAlert alert, BaseCustomParams params){
        IOSPushData data = new IOSPushData();
        data.setAlert(alert);
        data.setBadge(1);
        data.setPushCustomParams(params);

        return data;
    }

    /**
     * IOS 推送
     * @param data     推送内容
     * @param deviceToken    精确推送, 如果为空, 代表全局推送
     * @throws Exception
     */
    public void sendIOS(IOSPushData data,String deviceToken) throws Exception {

        PushConditions conditions = new PushConditions();
        conditions.setDeviceType(InstallationType.IOS.getCode());
        if(StringUtils.hasText(deviceToken)){
            conditions.setDeviceToken(deviceToken);
        }
        this.push(messagePushFormGenerator.generateForm(data,conditions));
    }


    private MessagePushResponse push(MessagePushForm form) throws Exception {

        MessagePushResponse response = null;
        try {
            response = leanCloudClient.send(URI,MessagePushResponse.class,form);

            if(!StringUtils.hasText(response.getObjectId()))
                throw new InvalidRequestException("LeanCloud return objectId is null.");

        } catch (Exception e) {
            logger.error("推送失败:" + e.getMessage());
            throw e;
        }

        return response;
    }
}
