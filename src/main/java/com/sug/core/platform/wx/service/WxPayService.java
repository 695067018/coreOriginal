package com.sug.core.platform.wx.service;

import com.sug.core.platform.web.rest.exception.APIErrorCode;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.platform.wx.model.*;
import com.sug.core.rest.client.SimpleHttpClient;
import com.sug.core.util.RandomStringGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


@Service
public class WxPayService {

    @Autowired
    private WxConfig wxConfig;

    private static final Logger log = LoggerFactory.getLogger(WxPayService.class);

    private static final String AUTHOR_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";

    private static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private static final String CREATEORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public String getAuthorizeUrl(String backUri) throws UnsupportedEncodingException {

        String backUrl = URLEncoder.encode(wxConfig.getOauthUrl() + backUri, "UTF-8");

        String url = AUTHOR_URL +
                "appid=" + wxConfig.getAppId()+
                "&redirect_uri=" +
                backUrl+
                "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

        log.debug("wx oauth url:"+url);

        return url;
    }

    /**
     * 获取openId
     * @param code
     * @return
     * @throws Exception
     */
    public WxPayOauthResp getOauth(String code) throws Exception {

        String url = GET_OPENID_URL + "?appid="
                +wxConfig.getAppId()+"&secret="
                +wxConfig.getAppSecret()+"&code="
                +code+"&grant_type=authorization_code";


        WxPayOauthResp resp = SimpleHttpClient.get(url, WxPayOauthResp.class);

        return resp;
    }


    /**
     * 用于网页支付
     * @param out_trade_no
     * @param fee
     * @param openId
     * @param spbill_create_ip
     * @return
     * @throws Exception
     */
    public WxPrepayResp getPrepayId(String out_trade_no, String body, Double fee, String openId, String spbill_create_ip) throws Exception {

        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        String mch_id = wxConfig.getMchId();

        int total_fee = (int) (fee * 100);
        String trade_type = "JSAPI";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", wxConfig.getAppId());
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);

        packageParams.put("total_fee", String.valueOf(total_fee));
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", wxConfig.getPayNotifyUrl());
        packageParams.put("trade_type", trade_type);
        packageParams.put("openid", openId);

        String sign = createSign(packageParams);

        String xml="<xml>"+
                "<appid>"+wxConfig.getAppId()+"</appid>"+
                "<mch_id>"+mch_id+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<sign>"+sign+"</sign>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                "<total_fee>"+ total_fee +"</total_fee>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<notify_url>"+wxConfig.getPayNotifyUrl()+"</notify_url>"+
                "<trade_type>"+trade_type+"</trade_type>"+
                "<openid>"+openId+"</openid>"+
                "</xml>";

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(CREATEORDERURL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);

        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");

        log.debug("unifiedOrder response text:" + responseText);

        Map map = doXMLParse(responseText);
        String prepay_id  = "prepay_id="+ map.get("prepay_id");

        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        finalpackage.put("appId", wxConfig.getAppId());
        finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        finalpackage.put("nonceStr", RandomStringGenerator.getRandomStringByLength(15));
        finalpackage.put("package", prepay_id);
        finalpackage.put("signType", "MD5");

        WxPrepayResp resp = new WxPrepayResp();
        resp.setAppId(finalpackage.get("appId"));
        resp.setNonceStr(finalpackage.get("nonceStr"));
        resp.setPackages(finalpackage.get("package"));
        resp.setSign(createSign(finalpackage));
        resp.setTimeStamp(finalpackage.get("timeStamp"));

        return resp;
    }

    /**
     * 用于app 支付
     * @param out_trade_no
     * @param fee
     * @param spbill_create_ip
     * @return
     * @throws Exception
     */
    public WxPrepayResp getPrepayId(String out_trade_no, String body, Double fee, String spbill_create_ip) throws Exception {

        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        String mch_id = wxConfig.getMchId();

        int total_fee = (int) (fee * 100);
        String trade_type = "APP";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", wxConfig.getAppId());
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);

        packageParams.put("total_fee", String.valueOf(total_fee));
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", wxConfig.getPayNotifyUrl());
        packageParams.put("trade_type", trade_type);

        String sign = createSign(packageParams);

        String xml="<xml>"+
                "<appid>"+wxConfig.getAppId()+"</appid>"+
                "<mch_id>"+mch_id+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<sign>"+sign+"</sign>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                "<total_fee>"+ total_fee +"</total_fee>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<notify_url>"+wxConfig.getPayNotifyUrl()+"</notify_url>"+
                "<trade_type>"+trade_type+"</trade_type>"+
                "</xml>";

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(CREATEORDERURL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);

        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");
        log.debug("unifiedOrder response text:" + responseText);

        Map map = doXMLParse(responseText);

        String packageValue = "Sign=WXPay";

        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        finalpackage.put("appid", wxConfig.getAppId());
        finalpackage.put("noncestr", RandomStringGenerator.getRandomStringByLength(15));
        finalpackage.put("package", packageValue);
        finalpackage.put("partnerid", wxConfig.getMchId());
        finalpackage.put("prepayid", (String) map.get("prepay_id"));
        finalpackage.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        WxPrepayResp resp = new WxPrepayResp();
        resp.setAppId(finalpackage.get("appid"));
        resp.setNonceStr(finalpackage.get("noncestr"));
        resp.setPackages(finalpackage.get("package"));
        resp.setPartnerId(finalpackage.get("partnerid"));
        resp.setPrepayId(finalpackage.get("prepayid"));
        resp.setTimeStamp(finalpackage.get("timestamp"));
        resp.setSign(createSign(finalpackage));


        return resp;
    }

    /**
     * 用于WEB 支付
     * @param out_trade_no
     * @param fee
     * @param spbill_create_ip
     * @return
     * @throws Exception
     */
    public WxPrepayResponse getNativePrepayId(String out_trade_no, String body, Double fee, String spbill_create_ip) throws Exception {

        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        String mch_id = wxConfig.getMchId();
        int total_fee = (int) (fee * 100);
        String trade_type = "NATIVE";

        WxPrepayRequest entity = new WxPrepayRequest();
        entity.setAppid(wxConfig.getAppId());
        entity.setMch_id(mch_id);
        entity.setNonce_str(nonce_str);
        entity.setBody(body);
        entity.setOut_trade_no(out_trade_no);
        entity.setTotal_fee(total_fee);
        entity.setSpbill_create_ip(spbill_create_ip);
        entity.setNotify_url(wxConfig.getPayNotifyUrl());
        entity.setTrade_type(trade_type);

        SortedMap<String, String> packageParams = entity.toMap();
        String sign = createSign(packageParams);
        entity.setSign(sign);

        JAXBContext jaxbContext = JAXBContext.newInstance(WxPrepayRequest.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(entity, sw);
        String xml = sw.toString();


        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(CREATEORDERURL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);

        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");
        log.debug("unifiedOrder response text:" + responseText);

        JAXBContext resJaxbContext = JAXBContext.newInstance(WxPrepayResponse.class);
        Unmarshaller unmarshaller = resJaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(responseText);
        WxPrepayResponse prepayResponse = (WxPrepayResponse) unmarshaller.unmarshal(reader);

        if(!prepayResponse.getReturn_code().equalsIgnoreCase("SUCCESS"))
            throw new InvalidRequestException(APIErrorCode.WX_PREPAY_FAILED.getCode(), "WX_PREPAY_FAILED");

        if(prepayResponse.getErr_code() == "ORDERPAID")
            throw new InvalidRequestException(APIErrorCode.WX_PREPAY_FAILED.getCode(), "该订单已支付");

        return prepayResponse;
    }

    public String getCallbackSign(Map<String, String> map) throws Exception {

        SortedMap<String, String> sortedMap = new TreeMap<String, String>();

        for(String keyValue : map.keySet()){
            if(keyValue.equalsIgnoreCase("sign"))
                continue;
            sortedMap.put(keyValue, map.get(keyValue));
        }

        String sign = createSign(sortedMap);
        log.debug("sign: " + sign);
        return sign;
    }


    private String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wxConfig.getPaySecret());

        String sign = MD5Util.MD5Encode(sb.toString(),"UTF-8")
                .toUpperCase();

        return sign;

    }


    public Map<String, String> doXMLParse(String strxml) throws Exception {
        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes());;
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        in.close();

        return m;
    }


    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public String buildResponseXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }
}
