package com.sug.core.platform.wx.request;

import java.util.List;

/**
 * Created by Greg.Chen on 2015/8/27.
 */
public class SendByOpenIdListForm {
    private List<String> touser;
    private SendAllMpNews mpnews;
    private String msgtype;


    public List<String> getTouser() {
        return touser;
    }

    public void setTouser(List<String> touser) {
        this.touser = touser;
    }

    public SendAllMpNews getMpnews() {
        return mpnews;
    }

    public void setMpnews(SendAllMpNews mpnews) {
        this.mpnews = mpnews;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}


