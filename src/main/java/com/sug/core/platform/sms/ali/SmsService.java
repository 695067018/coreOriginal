package com.sug.core.platform.sms.ali;

import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.json.JSONBinder;
import com.sug.core.platform.sms.ali.message.GeneralMessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Greg.chen on 2016-03-14.
 */
public abstract class SmsService<M> {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    public abstract void send(GeneralMessageForm form, M message) throws Exception;

    @Value("${alidy.appkey}")
    private String appkey;
    @Value("${alidy.secret}")
    private String secret;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    protected <M> String builderContent(GeneralMessageForm form, M message) {

        TreeMap<String, Object> apiparamsMap = new TreeMap<String, Object>();

        apiparamsMap.put("format", "json");

        apiparamsMap.put("method", "alibaba.aliqin.fc.sms.num.send");

        apiparamsMap.put("sign_method", "md5");

        apiparamsMap.put("app_key", appkey);

        apiparamsMap.put("v", "2.0");

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        apiparamsMap.put("timestamp", timestamp);

        apiparamsMap.put("sms_type", "normal");
        apiparamsMap.put("rec_num", form.getPhone());


        apiparamsMap.put("sms_free_sign_name", form.getSignName());
        apiparamsMap.put("sms_template_code", form.getTemplateCode());

        apiparamsMap.put("sms_param", JSONBinder.binder((Class<M>) message.getClass()).toJSON(message));
        //生成签名

        String sign = this.md5Signature(apiparamsMap, secret);

        apiparamsMap.put("sign", sign);

        StringBuilder param = new StringBuilder();

        for (Iterator<Map.Entry<String, Object>> it = apiparamsMap.entrySet()

                .iterator(); it.hasNext(); ) {

            Map.Entry<String, Object> e = it.next();

            param.append("&").append(e.getKey()).append("=").append(e.getValue().toString());

        }

        return param.toString().substring(1);
    }

    /**
     * 二行制转字符串
     */

    private String byte2hex(byte[] b) {

        StringBuffer hs = new StringBuffer();

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = (Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1)

                hs.append("0").append(stmp);

            else

                hs.append(stmp);

        }

        return hs.toString().toUpperCase();

    }


    private String md5Signature(TreeMap<String, Object> params, String secret) {

        String result = null;

        StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));

        if (orgin == null)

            return result;

        orgin.append(secret);

        result = MD5.encrypt(orgin.toString()).toUpperCase();

        return result;

    }



    private StringBuffer getBeforeSign(TreeMap<String, Object> params, StringBuffer orgin) {

        if (params == null)

            return null;

        Map<String, Object> treeMap = new TreeMap<String, Object>();

        treeMap.putAll(params);

        Iterator<String> iter = treeMap.keySet().iterator();

        while (iter.hasNext()) {

            String name = (String) iter.next();

            orgin.append(name).append(params.get(name).toString());

        }

        return orgin;

    }

    /**
     * 连接到TOP服务器并获取数据
     */

    protected void getResult(String urlStr, String content) throws Exception {

        logger.debug("aliMessage service connect start");

        URL url = null;

        HttpURLConnection connection = null;


        try {

            url = new URL(urlStr);

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);

            connection.setDoInput(true);

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);

            connection.connect();


            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.write(content.getBytes("utf-8"));

            out.flush();

            out.close();


            BufferedReader reader =

                    new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {

                buffer.append(line);

            }

            reader.close();

            String response = buffer.toString();

            if(response.indexOf("error") > -1){
                //logger.error();
                throw new Exception(response);
            }

            logger.debug("aliMessage response :" + response);
        }  finally {

            if (connection != null) {

                connection.disconnect();

            }

        }

    }
}
