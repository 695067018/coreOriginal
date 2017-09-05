package com.sug.core.platform.web.interceptor;

import com.sug.core.platform.exception.UserAuthorizationException;
import com.sug.core.platform.web.filter.RequestWrapper;
import com.sug.core.util.Convert;
import com.sug.core.util.StringUtils;
import com.sug.core.platform.http.HTTPHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by Greg.chen on 2016-04-12.
 */
public class BaseClientAuthInterceptor extends HandlerInterceptorAdapter {

    /**
     * 原始签名
     *
     * @param request
     * @return
     */
    public String buildSignedMessage(RequestWrapper request) {
        String timestamp = request.getHeader(HTTPHeaders.HEADER_TIMESTAMP);
        String requestId = request.getHeader(HTTPHeaders.HEADER_REQUEST_ID);
        String visitorId = request.getHeader(HTTPHeaders.HEADER_VISITOR_ID);

        StringBuilder builder = new StringBuilder(300);
        builder.append("uri=").append(request.getRequestURI());
        builder.append("&method=").append(request.getMethod().toUpperCase());
        if (StringUtils.hasText(request.getBody()))
            builder.append("&body=").append(request.getBody());
        builder.append("&timestamp=").append(timestamp);
        builder.append("&requestId=").append(requestId);
        if (StringUtils.hasText(visitorId))
            builder.append("&visitorId=").append(visitorId);

        return builder.toString();
    }

    public void isExpired(RequestWrapper request) {
        String timestamp = request.getHeader(HTTPHeaders.HEADER_TIMESTAMP);

        if (!StringUtils.hasText(timestamp))
            throw new UserAuthorizationException("timestamp header is required");

        Long clientTimestamp = Convert.toLong(timestamp, null);
        if (clientTimestamp == null) {
            throw new UserAuthorizationException("timestamp header is invalid");
        }
        Long serverTimeStamp = System.currentTimeMillis();
        if (clientTimestamp + (300 * 1000) < serverTimeStamp) {
            throw new UserAuthorizationException(String.format("request expired, clientTime=%s, serverTime=%s", clientTimestamp, serverTimeStamp));
        }
    }
}
