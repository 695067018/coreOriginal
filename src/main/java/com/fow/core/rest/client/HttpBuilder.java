package com.fow.core.rest.client;

import com.fow.core.util.UUIDUtils;

/**
 * Created by Greg.Chen on 2015/8/20.
 */
public class HttpBuilder {

    private String clientId;
    private String secretKey;
    private String visitorId;
    private String domain;
    private long timestamp;
    private String sessionId;
    //用户customer api 传入token
    private String customerId;
    private String requestId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HttpBuilder(String domain, String clientId, String secretKey, String visitorId, String sessionId) {
        this.clientId = clientId;
        this.secretKey = secretKey;
        this.visitorId = visitorId;
        this.domain = domain;
        this.timestamp = System.currentTimeMillis();
        this.sessionId = sessionId;
        this.requestId = UUIDUtils.create();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
