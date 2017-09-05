package com.sug.core.platform.leanCloud;

import com.sug.core.platform.json.JSONBinder;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.client.HTTPClient;
import com.sug.core.rest.client.HTTPConstants;
import com.sug.core.rest.client.HTTPException;
import com.sug.core.util.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2016-08-08.
 */
@Component
public class LeanCloudClient {
    private static Logger logger = LoggerFactory.getLogger(LeanCloudClient.class);

    @Value("${leanCloud.appId}")
    private String appId;
    @Value("${leanCloud.appKey}")
    private String appKey;

    private static final String CONTENT_TYPE = "application/json";

    private static final String BAD_REQUEST = "leanCloudBadRequest";

    public <T, U> T send(String url,Class<T> responseClass, U requestObj) throws Exception {

        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Post");
        logger.debug("====== http request begin ======");

        HttpPost request = new HttpPost(url);

        prepareHeaders(request);

        if(requestObj != null){
            String body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, "UTF-8");
            request.setEntity(entity);

            logger.debug("request body:"+ body);
        }

        HttpResponse response = HTTPClient.getHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    private void prepareHeaders(HttpRequestBase request){
        request.setHeader("X-LC-Id",appId);
        request.setHeader("X-LC-Key",appKey);
        request.setHeader("Content-Type",CONTENT_TYPE);
    }

    private static int getStatusCode(HttpResponse response){
        return  response.getStatusLine().getStatusCode();
    }

    private static String getResponseText(HttpResponse response) throws Exception {
        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "utf-8");

        return responseText;
    }

    private static void validateStatusCode(int responseCode, String responseText) {
        logger.debug("response status code => {}",responseCode);
        if (responseCode >= HTTPConstants.SC_OK && responseCode <= HTTPConstants.SC_MULTI_STATUS) {
            return;
        }
        logger.error(responseText);
        ErrorResponse response = JSONBinder.binder(ErrorResponse.class).fromJSON(responseText);
        if (responseCode == HTTPConstants.SC_BAD_REQUEST){
            throw new InvalidRequestException(BAD_REQUEST,response.getError());
        }
        else
            throw new HTTPException("invalid response status code, statusCode=" + responseCode + "error message:" + responseText);
    }
}
