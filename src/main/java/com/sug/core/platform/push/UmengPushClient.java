package com.sug.core.platform.push;

import com.sug.core.platform.push.android.AndroidBroadcast;
import com.sug.core.platform.push.android.AndroidNotification;
import com.sug.core.platform.push.android.AndroidUnicast;
import com.sug.core.platform.push.ios.IOSBroadcast;
import com.sug.core.util.StringUtils;
import com.sug.core.platform.push.ios.IOSUnicast;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class UmengPushClient {

	private static Logger logger = LoggerFactory.getLogger(UmengPushClient.class);

	// The user agent
	protected final String USER_AGENT = "Mozilla/5.0";

	// This object is used for sending the post request to Umeng
	protected HttpClient client = new DefaultHttpClient();
	
	// The host
	protected static final String host = "http://msg.umeng.com";
	
	// The upload path
	protected static final String uploadPath = "/upload";
	
	// The post path
	protected static final String postPath = "/api/send";

	@Value("${umeng.iOSAppkey}")
	private String iOSAppkey;
	@Value("${umeng.iOSAppMasterSecret}")
	private String iOSAppMasterSecret;
	@Value("${umeng.androidAppkey}")
	private String androidAppkey;
	@Value("${umeng.androidAppMasterSecret}")
	private String androidAppMasterSecret;
	@Value("${umeng.production_mode}")
	private Boolean productionMode;

	private String title;

	private String activityDomain;

	public String getiOSAppkey() {
		return iOSAppkey;
	}

	public void setiOSAppkey(String iOSAppkey) {
		this.iOSAppkey = iOSAppkey;
	}

	public String getiOSAppMasterSecret() {
		return iOSAppMasterSecret;
	}

	public void setiOSAppMasterSecret(String iOSAppMasterSecret) {
		this.iOSAppMasterSecret = iOSAppMasterSecret;
	}

	public String getAndroidAppkey() {
		return androidAppkey;
	}

	public void setAndroidAppkey(String androidAppkey) {
		this.androidAppkey = androidAppkey;
	}

	public String getAndroidAppMasterSecret() {
		return androidAppMasterSecret;
	}

	public void setAndroidAppMasterSecret(String androidAppMasterSecret) {
		this.androidAppMasterSecret = androidAppMasterSecret;
	}

	public void setProductionMode(Boolean productionMode) {
		this.productionMode = productionMode;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setActivityDomain(String activityDomain) {
		this.activityDomain = activityDomain;
	}

	@Async
	public void sendAll(String message,String activity,String key,String value) throws Exception{
		IOSBroadcast iosBroadcast = new IOSBroadcast(iOSAppkey,iOSAppMasterSecret);

		iosBroadcast.setAlert(message);
		iosBroadcast.setBadge(0);
		iosBroadcast.setSound("default");
		iosBroadcast.setCustomizedField(key,value);
		// TODO set 'production_mode' to 'true' if your app is under production mode
		if(productionMode){
			iosBroadcast.setProductionMode();
		}else {
			iosBroadcast.setTestMode();
		}

		this.send(iosBroadcast);

		AndroidBroadcast androidBroadcast = new AndroidBroadcast(androidAppkey,androidAppMasterSecret);

		androidBroadcast.setTicker(message);
		androidBroadcast.setTitle(this.title);
		androidBroadcast.setText(message);
		androidBroadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		androidBroadcast.setAfterOpenAction(AndroidNotification.AfterOpenAction.go_activity);
		androidBroadcast.setActivity(this.buildActivity(activity));
		androidBroadcast.setExtraField(key,value);

		if(productionMode){
			androidBroadcast.setProductionMode();
		}else {
			androidBroadcast.setTestMode();
		}

		this.send(androidBroadcast);
	}

	@Async
	public void sendUnique(String iOSDeviceToken,String androidDeviceToken,String message) throws Exception {
		if(StringUtils.hasText(iOSDeviceToken)){
			IOSUnicast iosUnicast = new IOSUnicast(iOSAppkey,iOSAppMasterSecret);

			iosUnicast.setDeviceToken(iOSDeviceToken);
			iosUnicast.setAlert(message);


			iosUnicast.setBadge(0);
			iosUnicast.setSound("default");
			// TODO set 'production_mode' to 'true' if your app is under production mode
			if(productionMode){
				iosUnicast.setProductionMode();
			}else {
				iosUnicast.setTestMode();
			}

			this.send(iosUnicast);
		}

		if(StringUtils.hasText(androidDeviceToken)){
			AndroidUnicast androidUnicast = new AndroidUnicast(androidAppkey,androidAppMasterSecret);

			androidUnicast.setDeviceToken(androidDeviceToken);
			androidUnicast.setTicker(message);
			androidUnicast.setTitle(this.title);
			androidUnicast.setText(message);
			androidUnicast.goAppAfterOpen();
			androidUnicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

			if(productionMode){
				androidUnicast.setProductionMode();
			}else {
				androidUnicast.setTestMode();
			}

			this.send(androidUnicast);
		}
	}

	public boolean send(UmengNotification msg) throws Exception {

		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		msg.setPredefinedKeyValue("timestamp", timestamp);
        String url = host + postPath;
        String postBody = msg.getPostBody();
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBody, "UTF-8");
        post.setEntity(se);
        // Send the post request and get the response
        HttpResponse response = client.execute(post);
        int status = response.getStatusLine().getStatusCode();
		logger.debug("Response Code : " + status);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (status == 200) {
            logger.debug("Notification sent successfully.");
			logger.debug(result.toString());
        } else {
			logger.error("Failed to send the notification!");
			logger.error(result.toString());
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

	private String buildActivity(String activity){
		return activityDomain + "." + activity;
	}
}
