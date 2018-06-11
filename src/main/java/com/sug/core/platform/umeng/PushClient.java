package com.sug.core.platform.umeng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PushClient {
	private static Logger logger = LoggerFactory.getLogger(PushClient.class);
	
	// The user agent
	private final String USER_AGENT = "Mozilla/5.0";

	// This object is used for sending the post request to Umeng
	private static final HttpClient client = new DefaultHttpClient();
	
	// The host
	private static final String host = "http://msg.umeng.com";
	
	// The upload path
	private static final String uploadPath = "/upload";
	
	// The post path
	private static final String postPath = "/api/send";

	@Autowired
	private RestTemplate restTemplate;

	public boolean send(UmengNotification msg) throws Exception {
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		msg.setPredefinedKeyValue("timestamp", timestamp);
        String url = host + postPath;
        String postBody = msg.getPostBody();
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
        url = url + "?sign=" + sign;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("User-Agent", USER_AGENT);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(msg.getPostBody(), httpHeaders);

        ResponseEntity<UmengResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity,UmengResponse.class);
		HttpStatus status = response.getStatusCode();

		/*HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);
        // Send the post request and get the response
		int status = response.getStatusLine().getStatusCode();
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}*/

//        logger.debug("Umeng send response code : "  + status);
        logger.debug("Umeng send response code : "  + status.value());
//        if (status == 200) {
        if (status.value() == 200) {
			logger.debug("Umeng Notification sent successfully");
        } else {
        	logger.error("Umeng Notification sent Failed : " + response.getBody().getData().getError_msg());
//        	logger.error("Umeng Notification sent Failed : " + result);
        }
        return true;
    }

	// Upload file with device_tokens to Umeng
	public String uploadContents(String appkey,String appMasterSecret,String contents) throws Exception {
		// Construct the json string
		JSONObject uploadJson = new JSONObject();
		uploadJson.put("appkey", appkey);
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		uploadJson.put("timestamp", timestamp);
		uploadJson.put("content", contents);
		// Construct the request
		String url = host + uploadPath;
		String postBody = uploadJson.toString();
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result.toString());
		// Decode response string and get file_id from it
		JSONObject respJson = new JSONObject(result.toString());
		String ret = respJson.getString("ret");
		if (!ret.equals("SUCCESS")) {
			throw new Exception("Failed to upload file");
		}
		JSONObject data = respJson.getJSONObject("data");
		String fileId = data.getString("file_id");
		// Set file_id into rootJson using setPredefinedKeyValue
		
		return fileId;
	}

}
