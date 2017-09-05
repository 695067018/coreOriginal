package com.sug.core.platform.web.rest.exception;

/**
 * Created by Administrator on 14-10-28.
 */
public enum APIErrorCode {

    SOCKET_TIMEOUT("SocketTimeout"),
    WX_PREPAY_FAILED("wxPrepayFailed"),
    SESSION_REQUIRED("SessionRequired"),
    SESSION_EXPIRED("SessionExpired"),
    LOGIN_REQUIRED("LoginRequired"),
    VALIDATION_ERROR("InvalidRequest"),
    VisitorNotFound("VisitorNotFound"),
    PERMISSION_REQUIRED("PermissionRequired"),
    CUSTOMER_EXISTS("CustomerExists");

    private APIErrorCode(String code){
        this.code = code;
    }


    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
