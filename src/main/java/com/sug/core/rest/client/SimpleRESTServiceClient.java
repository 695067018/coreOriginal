package com.sug.core.rest.client;

import com.sug.core.platform.exception.ForbiddenException;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.exception.UserAuthorizationException;
import com.sug.core.platform.exception.VisitorNotFoundException;
import com.sug.core.platform.http.HTTPHeaders;
import com.sug.core.platform.json.JSONBinder;
import com.sug.core.platform.web.rest.exception.APIErrorCode;
import com.sug.core.platform.web.rest.exception.ErrorResponse;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.util.CharacterEncodings;
import com.sug.core.util.StopWatch;
import com.sug.core.util.UUIDUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by greg.chen on 2014/10/21.
 */
public class SimpleRESTServiceClient {
    private static Logger logger = LoggerFactory.getLogger(SimpleRESTServiceClient.class);

    private String domain;

    public SimpleRESTServiceClient(String domain) {
        this.domain = domain;
    }


    private Map<String, String> headers;

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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
        HttpRequestBase request = new HttpGet(domain + uri);
        prepareHeaders(request);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}",getStatusCode(response), stopWatch.elapsedTime());
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);

        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public <T, U> T post(String uri, Class<T> responseClass, U requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", uri, "Post");
        logger.debug("====== http request begin ======");

        HttpPost request = new HttpPost(domain + uri);
        String body = null;

        if(requestObj != null){
            body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, CharacterEncodings.UTF_8);
            request.setEntity(entity);
        }
        prepareHeaders(request);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);
        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }


    public <T, U> T put(String uri, Class<T> responseClass, U requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", uri, "Put");
        logger.debug("====== http request begin ======");
        HttpPut request = new HttpPut(domain + uri);

        String body = null;
        if(requestObj != null){
            body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, CharacterEncodings.UTF_8);
            request.setEntity(entity);
        }

        prepareHeaders(request);


        HttpResponse response = HTTPClient.getHttpClient().execute(request);
        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public <T, U> T delete(String uri, Class<T> responseClass) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", uri, "Delete");
        logger.debug("====== http request begin ======");

        HttpRequestBase request = new HttpDelete(domain + uri);
        prepareHeaders(request);

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", response.getStatusLine().getStatusCode(), stopWatch.elapsedTime());
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
    private void prepareHeaders(HttpRequestBase request) throws Exception {
        request.setHeader(HTTPHeaders.HEADER_CONTENT_TYPE, HTTPConstants.CONTENT_TYPE_JSON);

        request.setHeader(HTTPHeaders.HEADER_REQUEST_ID, UUIDUtils.create());

        if(headers != null){
            for (Map.Entry<String, String> entry: headers.entrySet()) {

                request.setHeader(entry.getKey(),entry.getValue());
            }
        }
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

            if (APIErrorCode.VisitorNotFound.getCode().equals(errorResponse.getErrorCode())){
                throw new VisitorNotFoundException(errorResponse.getMessage());
            }

            throw new InvalidRequestException(errorResponse.getErrorCode(),errorResponse.getMessage());
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
}
