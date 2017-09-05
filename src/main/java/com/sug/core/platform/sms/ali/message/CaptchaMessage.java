package com.sug.core.platform.sms.ali.message;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Greg.chen on 2016-03-14.
 * 通用的发送短信验证码
 */
public class CaptchaMessage {
    private String code;

    private String product;

    public String getCode() {

        if(StringUtils.isEmpty(code)){
            String captcha = "";
            for (int i = 0; i < 4; i++) {
                captcha += (int) (Math.random() * 10);
            }
            this.code  =captcha;
        }

        return code;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public CaptchaMessage(String product){
        this.setProduct(product);
    }

}
