package com.fow.core.platform.web.rest.exception;

import java.util.List;

/**
 * Created by greg.chen on 14-10-14.
 */
public class ErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionTrace() {
        return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public FieldError getFieldError() {
        return fieldError;
    }

    public void setFieldError(FieldError fieldError) {
        this.fieldError = fieldError;
    }

    private String exceptionTrace;
    private String requestId;
    private String exceptionClass;
    private String errorCode;
    private FieldError fieldError;

}
