package com.sug.core.platform.wx.request;

/**
 * Created by A on 2015/8/27.
 */
public class SendAllForm {

    private SendAllFilter filter;
    private SendAllMpNews mpnews;
    private String msgtype;

    public SendAllFilter getFilter() {
        return filter;
    }

    public void setFilter(SendAllFilter filter) {
        this.filter = filter;
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
