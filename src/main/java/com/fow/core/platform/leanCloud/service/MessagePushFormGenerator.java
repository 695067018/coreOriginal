package com.fow.core.platform.leanCloud.service;

import com.fow.core.platform.leanCloud.request.push.MessagePushForm;
import com.fow.core.platform.leanCloud.request.push.PushConditions;
import com.fow.core.platform.leanCloud.request.push.PushData;
import com.fow.core.platform.leanCloud.request.push.android.AndroidPushData;
import com.fow.core.platform.leanCloud.request.push.ios.IOSPushData;
import com.fow.core.platform.web.rest.runtime.RuntimeEnvironment;
import com.fow.core.platform.web.rest.runtime.RuntimeSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2016-08-08.
 */
@Service
public class MessagePushFormGenerator{

    @Value("${leanCloud.push.expiration_interval_second:86400}")  //默认24 小时后过期
    private Integer expiration_interval;

    @Autowired
    private RuntimeSettings runtimeSettings;

    private Integer getExpiration_interval() {
        return expiration_interval;
    }

    private Date getExpiration_time(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, expiration_interval * 1000);

        return calendar.getTime();
    }

    private String getCertificate(){
        if(runtimeSettings.getEnvironment().equals(RuntimeEnvironment.dev)){
            return "dev";
        }else {
            return "prod";
        }
    }

    public MessagePushForm generateForm(IOSPushData data, PushConditions conditions){
        MessagePushForm form = generateBaseForm(data,conditions);
        form.setProd(getCertificate());
        return form;
    }

    public MessagePushForm generateForm(AndroidPushData data,PushConditions conditions){
        return generateBaseForm(data,conditions);
    }

    private MessagePushForm generateBaseForm(PushData data,PushConditions conditions){
        MessagePushForm form = new MessagePushForm();
        form.setData(data);
        form.setExpiration_interval(getExpiration_interval());
        form.setExpiration_time(getExpiration_time());
        form.setWhere(conditions);

        return form;
    }
}
