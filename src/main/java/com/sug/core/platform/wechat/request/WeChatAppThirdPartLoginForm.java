package com.sug.core.platform.wechat.request;

import org.hibernate.validator.constraints.NotEmpty;

public class WeChatAppThirdPartLoginForm {
    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
