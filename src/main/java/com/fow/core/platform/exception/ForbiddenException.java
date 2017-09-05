package com.fow.core.platform.exception;

/**
 * Created by Administrator on 2015/12/15.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
