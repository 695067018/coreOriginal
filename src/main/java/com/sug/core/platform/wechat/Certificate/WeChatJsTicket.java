package com.sug.core.platform.wechat.Certificate;

import java.util.Date;

public class WeChatJsTicket {
    private String jsTicket;

    private Date generateTime;

    public String getJsTicket() {
        return jsTicket;
    }

    public void setJsTicket(String jsTicket) {
        this.jsTicket = jsTicket;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }
}
