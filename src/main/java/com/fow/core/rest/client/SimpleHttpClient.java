package com.fow.core.rest.client;

import com.fow.core.platform.json.JSONBinder;
import com.fow.core.util.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by greg.chen on 2014/10/21.
 */
public class SimpleHttpClient {
    private static Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static final String CHARSET_UTF_8 = "UTF-8";

    public SimpleHttpClient(){}

    private static int getStatusCode(HttpResponse response){
        return  response.getStatusLine().getStatusCode();
    }

    private static String getResponseText(HttpResponse response) throws Exception {
        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, CHARSET_UTF_8);

        return responseText;
    }

    public static  <T> T get(String url, Class<T> responseClass) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Get");
        logger.debug("====== http request begin ======");
        HttpRequestBase request = new HttpGet(url);
        prepareHeaders(request);

        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}",getStatusCode(response), stopWatch.elapsedTime());
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);
        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public static  <T> T get(String url, Class<T> responseClass, Map<String,String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String,String> entry : params.entrySet()){
                uriBuilder.addParameter(entry.getKey(),entry.getValue());
            }
        }

        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Get");
        logger.debug("====== http request begin ======");
        HttpRequestBase request = new HttpGet(uriBuilder.build());
        prepareHeaders(request);

        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}",getStatusCode(response), stopWatch.elapsedTime());
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);
        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }


    public static String getResponseText(String url) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Get");
        logger.debug("====== http request begin ======");
        HttpRequestBase request = new HttpGet(url);
        prepareHeaders(request);

        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}",getStatusCode(response), stopWatch.elapsedTime());
        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);
        return responseText;
    }

    public static <T, U> T post(String url, Class<T> responseClass, U requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Post");
        logger.debug("====== http request begin ======");
        HttpPost request = new HttpPost(url);

        if(requestObj != null){
            String body = JSONBinder.binder((Class<U>) requestObj.getClass()).toJSON(requestObj);
            AbstractHttpEntity entity = new StringEntity(body, CHARSET_UTF_8);
            request.setEntity(entity);
        }

        prepareHeaders(request);
        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public static <T> T postWithFormData(String url, Class<T> responseClass, Map<String,String> requestObj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Post");
        logger.debug("====== http request begin ======");
        HttpPost request = new HttpPost(url);

        if(requestObj != null && !requestObj.isEmpty()){
            List<NameValuePair> paramsList = new ArrayList<>();

            for(Map.Entry<String,String> entry : requestObj.entrySet()){
                paramsList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }

            request.setEntity(new UrlEncodedFormEntity(paramsList,"utf-8"));
        }

        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);
        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public static <T, U> T uploadImage(String url, Class<T> responseClass, File file) throws Exception {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "Post");
        logger.debug("====== http request begin ======");

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while((len = fis.read(buffer)) != -1){
            outputStream.write(buffer, 0 , len);
        }

        byte[] data = outputStream.toByteArray();

        HttpPost request = new HttpPost(url);
        ByteArrayBody byteArrayBody = new ByteArrayBody(data, file.getName());
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("media", byteArrayBody);
        request.setEntity(reqEntity);

        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        int responseCode = getStatusCode(response);
        String responseText = getResponseText(response);

        request.releaseConnection();
        validateStatusCode(responseCode, responseText);

        fis.close();
        return JSONBinder.binder(responseClass).fromJSON(responseText);
    }

    public static void downloadImage(String url, File file) throws IOException {
        StopWatch stopWatch = new StopWatch();
        logger.debug("send request, url={}, method={}", url, "get");
        logger.debug("====== http request begin ======");

        HttpRequestBase request = new HttpGet(url);
        HttpResponse response =createDefaultHttpClient().execute(request);

        logger.debug("====== http request end ======");
        logger.debug("received response, statusCode={}, elapsed={}", getStatusCode(response), stopWatch.elapsedTime());

        BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int inByte;
        while((inByte = bis.read()) !=-1){
            bos.write(inByte);
        }

        bis.close();
        bos.close();
    }


    /**
     * 写入请求头
     * @param request
     */
    private static void prepareHeaders(HttpRequestBase request) {
        request.setHeader("Content-Type", HTTPConstants.CONTENT_TYPE_JSON);
    }

    /**
     * 验证Response code
     * @param responseCode
     * @param responseText
     */
    private static void validateStatusCode(int responseCode, String responseText) {
        logger.debug("response status code => {}",responseCode);
        if (responseCode >= HTTPConstants.SC_OK && responseCode <= HTTPConstants.SC_MULTI_STATUS) {
            return;
        }
        logger.error(responseText);
        throw new HTTPException("invalid response status code, statusCode=" + responseCode + "error message:" + responseText);
    }

    /**
     * 获取HttpClient
     * @return
     */
    private static HttpClient createDefaultHttpClient() {
        HttpClient client = new DefaultHttpClient();

        return client;
    }
}
