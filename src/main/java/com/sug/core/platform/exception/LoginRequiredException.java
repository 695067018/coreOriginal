package com.sug.core.platform.exception;

/**
 * Created by Administrator on 14-10-28.
 */
public class LoginRequiredException extends RuntimeException {
    public LoginRequiredException(String message) {
        super(message);
    }

    public LoginRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
