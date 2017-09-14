package com.sug.core.platform.wechat.form;

public class WeChatNativeParamsForm {
    private Double fee;
    private String body;
    private String paymentNum;
    private String productId;
    private String apiIP;

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPaymentNum() {
        return paymentNum;
    }

    public void setPaymentNum(String paymentNum) {
        this.paymentNum = paymentNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getApiIP() {
        return apiIP;
    }

    public void setApiIP(String apiIP) {
        this.apiIP = apiIP;
    }
}
