package com.sug.core.platform.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WeChatJsPayResponse {

    private String nonceStr;
    private Long timestamp;
    @JsonProperty("package")
    private String packageBody;
    private String signType;
    private String paySign;

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackageBody() {
        return packageBody;
    }

    public void setPackageBody(String packageBody) {
        this.packageBody = packageBody;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
