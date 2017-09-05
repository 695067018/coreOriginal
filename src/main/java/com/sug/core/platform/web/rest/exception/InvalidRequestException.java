package com.sug.core.platform.web.rest.exception;

/**
 * Created by greg.chen on 14-10-23.
 */
public class InvalidRequestException extends RuntimeException {
    private String code;

    public InvalidRequestException(String message) {
        this(null, message, null);
    }

    public InvalidRequestException(String message, Throwable cause) {
        this(null, message, cause);
    }

    public InvalidRequestException(String code, String message) {
        this(code, message, null);
    }

    public InvalidRequestException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
