package com.sug.core.platform.sms.ali.message;

/**
 * Created by Greg.chen on 2016-03-14.
 */
public class GeneralMessageForm {

    private String signName;

    private String templateCode;

    private String phone;

    public GeneralMessageForm(String signName, String templateCode, String phone) {
        this.signName = signName;
        this.templateCode = templateCode;
        this.phone = phone;
    }

    public String getSignName() {
        return signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public String getPhone() {
        return phone;
    }
}
