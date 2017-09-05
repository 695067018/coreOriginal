package com.fow.core.platform.exception;

/**
 * Created by greg.chen on 14-10-21.
 */
public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException(String message) {
        super(message);
    }

    public SessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
