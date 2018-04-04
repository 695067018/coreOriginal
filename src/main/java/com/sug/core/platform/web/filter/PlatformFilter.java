package com.sug.core.platform.web.filter;

import com.sug.core.platform.http.HTTPHeaders;
import com.sug.core.platform.log.ActionLog;
import com.sug.core.platform.log.ActionLogger;
import com.sug.core.platform.log.TraceLogger;
import com.sug.core.platform.web.request.RemoteAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Greg.Chen on 2015/5/22.
 */
public class PlatformFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(PlatformFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest originalRequest = (HttpServletRequest) servletRequest;
        RequestWrapper requestWrapper = new RequestWrapper(originalRequest);
        //不记录监控日志
        if(HttpMethod.HEAD.matches(requestWrapper.getMethod()))
        {
            filterChain.doFilter(requestWrapper, servletResponse);
            return;
        }

        TraceLogger traceLogger = TraceLogger.getInstance();
        try{
            traceLogger.initialize();
            MDC.put("startTime", String.valueOf(System.currentTimeMillis()));
            logger.info("=== begin request processing ===");


            logRequest(requestWrapper);

            filterChain.doFilter(requestWrapper, servletResponse);
        }
        finally {
            logResponse((HttpServletResponse) servletResponse);
            logger.info("=== finish request processing ===");
            traceLogger.cleanup();
        }
    }

    private void logResponse(HttpServletResponse response) {
        int status = response.getStatus();
        logger.info("responseHTTPStatus={}", status);
        logHeaders(response);
    }

    private void logHeaders(HttpServletResponse response) {
        for (String name : response.getHeaderNames()) {
            logger.info("[response-header] {}={}", name, response.getHeader(name));
        }
    }

    private void logRequest(RequestWrapper requestWrapper) {

        logger.info("originalRequestURL={}", requestWrapper.getRequestURI());
        logger.info("originalServerPort={}", requestWrapper.getServerPort());
        logger.info("originalContextPath={}", requestWrapper.getContextPath());
        logger.info("originalMethod={}", requestWrapper.getMethod());
        logger.info("dispatcherType={}", requestWrapper.getDispatcherType());
        logger.info("serverPort={}", requestWrapper.getServerPort());
        logger.info("localPort={}", requestWrapper.getLocalPort());
        logHeaders(requestWrapper);
        logParameters(requestWrapper);
        if (requestWrapper.isPreLoadBody()) {
            logger.info("body={}", requestWrapper.getBody());
        }

    }

    private void logHeaders(RequestWrapper requestWrapper) {
        Enumeration headers = requestWrapper.getHeaderNames();
        while(headers.hasMoreElements()){
            String headerName = (String) headers.nextElement();
            logger.debug("[header] {}={}", headerName, requestWrapper.getHeader(headerName));
        }
    }

    private void logParameters(RequestWrapper requestWrapper) {
        Enumeration paramNames = requestWrapper.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            logger.info("[param] {}={}", paramName, requestWrapper.getParameter(paramName));
        }
    }

    @Override
    public void destroy() {

    }
}
