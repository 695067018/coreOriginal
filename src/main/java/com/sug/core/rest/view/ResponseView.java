package com.sug.core.rest.view;

public class ResponseView {
    private String code;

    private String message;

    public ResponseView() {
        this.code = "success";
        this.message = "success";
    }

    public ResponseView(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
