package com.fow.core.rest.client;

import com.fow.core.platform.crypto.HMAC;
import com.fow.core.platform.exception.*;
import com.fow.core.platform.http.HTTPHeaders;
import com.fow.core.platform.json.JSONBinder;
import com.fow.core.platform.web.rest.exception.APIErrorCode;
import com.fow.core.platform.web.rest.exception.ErrorResponse;
import com.fow.core.platform.web.rest.exception.FieldError;
import com.fow.core.platform.web.rest.exception.InvalidRequestException;
import com.fow.core.util.CharacterEncodings;
import com.fow.core.util.EncodingUtils;
import com.fow.core.util.StopWatch;
import com.fow.core.util.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.nio.charset.Charset;

/**
 * Created by greg.chen on 2014/10/21.
 */
public class RESTServiceClient {
    private static Logger logger = LoggerFactory.getLogger(RESTServiceClient.class);

    public int timeout;

    private HttpBuilder httpBuilder;

    public RESTServiceClient(){}

    public RESTServiceClient(HttpBuilder httpBuilder){
        this.httpBuilder = httpBuilder;
    }

    public HttpBuilder getHttpBuilder() {
        return httpBuilder;
    }



    private int getStatusCode(HttpResponse response){
        return  response.getStatusLine().getStatusCode();
    }

    private String getResponseText(HttpResponse response) throws Exception {
        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, CharacterEncodings.UTF_8);

        return responseText;
    }


    public <T> T get(String uri, Class<T> responseClass) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", uri, "Get");
        logger.debug("====== http request begin ======");
        HttpRequestBase request = new HttpGet(this.httpBuilder.getDomain() + uri);
        prepareHeaders(request, uri, HttpMethod.GET.name(), null);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}",getStatusCode(response), stopWatch.elapsedTime());
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        setSessionId(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public <T, U> T post(String uri, Class<T> responseClass, U requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", this.httpBuilder.getDomain() + uri, "Post");
        logger.debug("====== http request begin ======");

        HttpPost request = new HttpPost(this.httpBuilder.getDomain() + uri);
        String body = null;

        if(requestObj != null){
            body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, CharacterEncodings.UTF_8);
            request.setEntity(entity);
        }
        prepareHeaders(request, uri, HttpMethod.POST.name(), body);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());
        setSessionId(response);
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);
        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }


    public <T, U> T put(String uri, Class<T> responseClass, U requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", this.httpBuilder.getDomain() + uri, "Put");
        logger.debug("====== http request begin ======");
        HttpPut request = new HttpPut(this.httpBuilder.getDomain() + uri);

        String body = null;
        if(requestObj != null){
            body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, CharacterEncodings.UTF_8);
            request.setEntity(entity);
        }

        prepareHeaders(request, uri, HttpMethod.PUT.name(), body);


        HttpResponse response = HTTPClient.getHttpClient().execute(request);
        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        setSessionId(response);
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }


    public <T, U> T delete(String uri, Class<T> responseClass) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", this.httpBuilder.getDomain() + uri, "Delete");
        logger.debug("====== http request begin ======");

        HttpRequestBase request = new HttpDelete(this.httpBuilder.getDomain() + uri);
        prepareHeaders(request, uri, HttpMethod.DELETE.name(), null);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", response.getStatusLine().getStatusCode(), stopWatch.elapsedTime());
        setSessionId(response);
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    /**
     * 写入请求头
     * @param request
     */
    private void prepareHeaders(HttpRequestBase request, String uri, String method, String body) throws Exception {
        request.setHeader(HTTPHeaders.HEADER_CONTENT_TYPE, HTTPConstants.CONTENT_TYPE_JSON);
        request.setHeader(HTTPHeaders.HEADER_CLIENT_ID,this.httpBuilder.getClientId());
        request.setHeader(HTTPHeaders.HEADER_VISITOR_ID,this.httpBuilder.getVisitorId());
        request.setHeader(HTTPHeaders.HEADER_TIMESTAMP, String.valueOf(this.httpBuilder.getTimestamp()));

        if(StringUtils.hasText(this.getHttpBuilder().getSessionId()))
            request.setHeader(HTTPHeaders.HEADER_COOKIE,"JSESSIONID=" + this.getHttpBuilder().getSessionId());

        if(StringUtils.hasText(this.getHttpBuilder().getCustomerId()))
            request.setHeader(HTTPHeaders.HEADER_CUSTOMER_ID,this.getHttpBuilder().getCustomerId());

        if(StringUtils.hasText(this.getHttpBuilder().getRequestId()))
            request.setHeader(HTTPHeaders.HEADER_REQUEST_ID,this.getHttpBuilder().getRequestId());

        String signature = builderSignature(uri, method, body);
        request.setHeader(HTTPHeaders.HEADER_CLIENT_SIGNATURE , signature);
    }

    private String builderSignature(String uri, String method, String body) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("uri=" + uri);
        sb.append("&method=" + method.toUpperCase());
        if(StringUtils.hasText(body)){
            sb.append("&body=" + body);
        }
        sb.append("&timestamp=" + this.httpBuilder.getTimestamp());
        sb.append("&requestId=" + this.httpBuilder.getRequestId());
        if(StringUtils.hasText(this.httpBuilder.getVisitorId()))
            sb.append("&visitorId=" + this.httpBuilder.getVisitorId());


        return EncodingUtils.encryptBASE64(HMAC.encryptHMAC(sb.toString().getBytes(Charset.forName("UTF-8"))
                , this.httpBuilder.getSecretKey()));
    }


    /**
     * 验证Response code
     * @param responseCode
     * @param responseText
     */
    private void validateStatusCode(int responseCode, String responseText) {
        logger.debug("response status code => {}",responseCode);
        if (responseCode >= HTTPConstants.SC_OK && responseCode <= HTTPConstants.SC_MULTI_STATUS) {
            return;
        }
        ErrorResponse errorResponse = JSONBinder.binder(ErrorResponse.class).fromJSON(responseText);

        if (responseCode == HTTPConstants.SC_BAD_REQUEST) {

            if(APIErrorCode.LOGIN_REQUIRED.getCode().equals(errorResponse.getErrorCode())){
                throw new LoginRequiredException(errorResponse.getMessage());
            } else if(APIErrorCode.SESSION_EXPIRED.getCode().equals(errorResponse.getErrorCode())){
                throw new SessionExpiredException(errorResponse.getMessage());
            } else if (APIErrorCode.VisitorNotFound.getCode().equals(errorResponse.getErrorCode())){
                throw new VisitorNotFoundException(errorResponse.getMessage());
            }
            else {
                FieldError fieldError = errorResponse.getFieldError();
                if(fieldError != null)
                    throw new InvalidRequestException(fieldError.getField(), fieldError.getMessage());
                else
                    throw new InvalidRequestException(errorResponse.getErrorCode(),errorResponse.getMessage());
            }
        }
        else if(responseCode == HTTPConstants.SC_NOT_FOUND){
            throw new ResourceNotFoundException(errorResponse.getMessage());
        }
        else if(responseCode == HTTPConstants.SC_UNAUTHORIZED){
            throw new UserAuthorizationException(errorResponse.getMessage());
        }
        else if(responseCode == HTTPConstants.SC_FORBIDDEN){
            throw new ForbiddenException(errorResponse.getMessage());
        }

        logger.error(responseText);
        throw new HTTPException("invalid response status code, statusCode=" + responseCode + "error message:" + responseText);
    }




    private void setSessionId(HttpResponse response) {
        Header header = response.getFirstHeader("Set-Cookie");
        if(header != null){
            HeaderElement[] elements = header.getElements();
            for (HeaderElement element : elements){
                if("JSESSIONID".equalsIgnoreCase(element.getName())){
                    this.httpBuilder.setSessionId(element.getValue());
                }
            }
        }
    }
}
