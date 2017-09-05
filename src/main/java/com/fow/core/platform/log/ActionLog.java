package com.fow.core.platform.log;

import java.util.Date;
import java.util.Map;

/**
 * Created by Greg.chen on 2016-03-15.
 */
public class ActionLog {
    private final Date requestDate = new Date();
    private String requestId;
    private String action;
    private ActionResult result = ActionResult.SUCCESS;
    private String exception;
    private String errorMessage;
    private String traceLogPath;
    private long elapsedTime;
    private String clientIP;
    private String requestURI;
    private String httpMethod;
    private Integer httpStatusCode;

    public Date getRequestDate() {
        return requestDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ActionResult getResult() {
        return result;
    }

    public void setResult(ActionResult result) {
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getHTTPMethod() {
        return httpMethod;
    }

    public void setHTTPMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getTraceLogPath() {
        return traceLogPath;
    }

    public void setTraceLogPath(String traceLogPath) {
        this.traceLogPath = traceLogPath;
    }

    public Integer getHTTPStatusCode() {
        return httpStatusCode;
    }

    public void setHTTPStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
