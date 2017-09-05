/**
 * 
 */
package com.sug.core.platform.wx.service;

import com.sug.core.platform.wx.request.*;
import com.sug.core.platform.wx.view.SendAllView;
import com.sug.core.rest.client.SimpleHttpClient;
import com.sug.core.platform.wx.view.SendView;
import com.sug.core.platform.wx.view.UploadImageView;
import com.sug.core.util.RandomStringGenerator;
import com.sug.core.platform.wx.view.NewsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author greg.chen
 * @version 2015/08/27
 *
 */
@Service
public class WxMessageService {

	@Autowired
	private Environment environment;

	private final static String UPLOAD_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/media/upload";
	private final static String UPLOAD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
	private final static String SEND_ALL_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
	private final static String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send";

	public UploadImageView uploadImage(String path, String token) throws Exception {

		String url = UPLOAD_IMAGE_URL + "?access_token=" + token + "&type=image";

		String tmpPath = environment.getProperty("settings.tmp");

		File dirFile = new File(tmpPath);
		if(!dirFile.exists())
			dirFile.mkdirs();

		File file = new File(tmpPath + "/" + RandomStringGenerator.getRandomStringByLength(6) + ".jpg");
		file.createNewFile();
		SimpleHttpClient.downloadImage(path, file);

		UploadImageView wxUploadImage =  SimpleHttpClient.uploadImage(url, UploadImageView.class, file);
		file.delete();
		return wxUploadImage;
	}

	public NewsView uploadNews(List<Article> articles, String token) throws Exception {

		String url = UPLOAD_NEWS_URL + "?access_token=" + token;

		NewsForm form = new NewsForm(articles);

		NewsView newsView = SimpleHttpClient.post(url, NewsView.class,form);

		return newsView;
	}

	public SendAllView sendAll(String token, String media_id, String groupId) throws Exception {

		String url = SEND_ALL_URL +  "?access_token=" + token;

		SendAllForm form = new SendAllForm();
		form.setFilter(new SendAllFilter(false, groupId));

		form.setMpnews(new SendAllMpNews(media_id));
		form.setMsgtype("mpnews");

		SendAllView view = SimpleHttpClient.post(url, SendAllView.class,form);

		return view;
	}

	public SendView sendByOpenIds(String token, String media_id, List<String> openIds) throws Exception {
		String url = SEND_URL +  "?access_token=" + token;

		SendByOpenIdListForm form = new SendByOpenIdListForm();
		form.setTouser(openIds);
		form.setMsgtype("mpnews");
		form.setMpnews(new SendAllMpNews(media_id));

		SendView view = SimpleHttpClient.post(url, SendView.class,form);

		return view;
	}
}

