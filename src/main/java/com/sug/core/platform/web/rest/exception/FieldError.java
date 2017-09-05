package com.sug.core.platform.web.rest.exception;

/**
 * Created by greg.chen on 14-10-20.
 */
public class FieldError {

    private String field;

    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
