package com.sug.core.platform.dingtalk.request;

import com.sug.core.platform.dingtalk.domain.SendMsgText;

public class DingtalkSendMsgForm {
    private String chatid;
    private String msgtype;
    private SendMsgText text;

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public SendMsgText getText() {
        return text;
    }

    public void setText(SendMsgText text) {
        this.text = text;
    }
}
