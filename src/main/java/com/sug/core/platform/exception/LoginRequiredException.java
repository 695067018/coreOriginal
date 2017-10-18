package com.sug.core.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Administrator on 14-10-28.
 */
@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
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
