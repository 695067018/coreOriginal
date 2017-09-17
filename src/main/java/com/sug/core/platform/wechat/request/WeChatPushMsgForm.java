package com.sug.core.platform.wechat.request;


import com.sug.core.platform.wechat.form.WeChatMsgData;

import java.util.Map;

public class WeChatPushMsgForm {
    private String touser;
    private String template_id;
    private WeChatMsgData data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public WeChatMsgData getData() {
        return data;
    }

    public void setData(WeChatMsgData data) {
        this.data = data;
    }
}
