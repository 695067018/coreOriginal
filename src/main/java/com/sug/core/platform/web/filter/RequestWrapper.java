package com.sug.core.platform.web.filter;

import com.sug.core.util.IOUtils;
import com.sug.core.rest.client.HTTPConstants;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created by A on 2015/5/25.
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private String body;
    private ServletInputStream inputStream;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        preLoadBody(request);
    }

    private void preLoadBody(HttpServletRequest request) throws IOException {
        if(isPreLoadBody() && !request.getRequestURI().contains("alipay")){
            ServletInputStream originalInputStream = request.getInputStream();
            byte[] bodyBytes = IOUtils.bytes(originalInputStream);

            this.body = new String(bodyBytes, "UTF-8");

            inputStream = new RequestCachingInputStream(body.getBytes("UTF-8"));
        }

    }


    public boolean isPreLoadBody() {

        return containsBody() && !isMultipart();
    }

    private boolean containsBody() {
        String originalMethod = super.getMethod().toUpperCase();
        return HTTPConstants.METHOD_POST.equals(originalMethod) || HTTPConstants.METHOD_PUT.equals(originalMethod);
    }

    private boolean isMultipart() {
        String contentType = getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    public String getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null)
            return inputStream;
        return super.getInputStream();
    }
}
