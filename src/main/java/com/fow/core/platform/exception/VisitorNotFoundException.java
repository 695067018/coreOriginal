package com.fow.core.platform.exception;

/**
 * Created by greg.chen on 14-10-21.
 */
public class VisitorNotFoundException extends RuntimeException {
    public VisitorNotFoundException(String message) {
        super(message);
    }

    public VisitorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
