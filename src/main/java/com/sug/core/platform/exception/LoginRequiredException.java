package com.sug.core.platform.exception;

/**
 * Created by Administrator on 14-10-28.
 */
public class LoginRequiredException extends RuntimeException {
    private String code;

    public LoginRequiredException(String code,String message){
        super(message);
        this.code = code;
    }

    public LoginRequiredException(String message) {
        super(message);
    }

    public LoginRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }
}
