package com.sug.core.platform.wechat.service;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.wechat.constants.WeChatJsInter;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.*;
import com.sug.core.platform.wechat.request.WeChatPayNotifyForm;
import com.sug.core.platform.wechat.response.*;
import com.sug.core.platform.wx.model.WxPrepayRequest;
import com.sug.core.platform.wx.model.WxPrepayResponse;
import com.sug.core.platform.wx.service.MD5Util;
import com.sug.core.util.RandomStringGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.*;

@Service
public class WeChatPayService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayService.class);

    private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final String GIFTMONEY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    private static final String CHECK_PAYMENT = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Autowired
    private WeChatParams params;

    @Value("${weChat.giftMoney.sendName:@null}")
    private String giftMoneySendName;

    @Value("${website.ip:@null}")
    private String websiteIP;

    @Value("${weChat.ca.path}")
    private String caPath;

    private static HttpClient giftClient = new DefaultHttpClient();

    @PostConstruct
    public void initGiftClient() throws Exception {
        if(!StringUtils.hasText(caPath)){
            return;
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        FileInputStream inputStream = new FileInputStream(new File(caPath));
        try {
            keyStore.load(inputStream, params.getMchId().toCharArray());
        } finally {
            inputStream.close();
        }

        SSLContext sslcontext = SSLContexts
                .custom()
                .loadKeyMaterial(keyStore, params.getMchId().toCharArray())
                .build();
        SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);
        Scheme sch = new Scheme("https", 8443, socketFactory);
        giftClient.getConnectionManager().getSchemeRegistry().register(sch);
    }

    @Autowired
    private WeChatSignService signService;

    @Autowired
    private static HttpClient httpClientWithCa;

    public WeChatJsPayResponse getJsPayParams(WeChatJsPayParamsForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        Long timestamp = System.currentTimeMillis();

        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        unifiedOrderForm.setAppid(params.getMpAppId());
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_JS);

        //generate unified order
        String packageBody = "prepay_id=" + this.generateUnifiedorder(unifiedOrderForm).getPrepay_id();

        WeChatPaySignForm signForm = new WeChatPaySignForm();
        signForm.setTimeStamp(timestamp.toString());
        signForm.setNonceStr(nonce_str);
        signForm.setPackageBody(packageBody);

        WeChatJsPayResponse response = new WeChatJsPayResponse();
        BeanUtils.copyProperties(signForm,response);
        response.setTimestamp(timestamp);
        response.setSignType(WeChatPayConstants.SIGNTYPE_JSPAY);
        response.setPaySign(signService.jsPaySign(signForm));

        return response;
    }

    public String getQrCodeUrl(WeChatNativeParamsForm form) throws Exception {
        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setAppid(params.getMpAppId());
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_NATIVE);

        //generate unified order
        return this.generateUnifiedorder(unifiedOrderForm).getCode_url();
    }

    public String getMweburl(WeChatWebParamsForm form) throws Exception {
        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setAppid(params.getMpAppId());
        //这里设置是那种支付方式
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_H5);
        //generate unified order
        return this.generateUnifiedorder(unifiedOrderForm).getMweb_url();
    }

    public WeChatAppPayResponse getAppPayParams(WeChatAppPayParamsForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        Long timestamp = System.currentTimeMillis() / 1000;

        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setAppid(params.getOpenAppId());
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_APP);

        //generate unified order
        String prepayId = this.generateUnifiedorder(unifiedOrderForm).getPrepay_id();

        WeChatAppPayResponse response = new WeChatAppPayResponse();
        response.setAppid(params.getOpenAppId());
        response.setPartnerid(params.getMchId());
        response.setPrepayid(prepayId);
        response.setPackageBody("Sign=WXPay");
        response.setNoncestr(nonce_str);
        response.setTimestamp(timestamp.toString());

        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appid",params.getOpenAppId());
        map.put("partnerid",params.getMchId());
        map.put("prepayid",prepayId);
        map.put("noncestr",nonce_str);
        map.put("timestamp",timestamp.toString());
        map.put("package","Sign=WXPay");

        response.setSign(signService.unifiedPaySign(map));

        return response;
    }

    private WeChatUnifiedOrderResponse generateUnifiedorder(WeChatUnifiedOrderForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        form.setNonce_str(nonce_str);
        form.setMch_id(params.getMchId());
        form.setNotify_url(params.getNotifyUrl());

        String sign = signService.unifiedPaySign(form.toMap());

        form.setSign(sign);
        //把参数转为xml
        JAXBContext jaxbContext = JAXBContext.newInstance(WeChatUnifiedOrderForm.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(form, sw);
        String xml = sw.toString();
        //用xml格式参数去请求微信得到微信响应的结果
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(UNIFIEDORDER_URL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);
        //拿出微信响应的结果xml转为java
        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");

        JAXBContext resJaxbContext = JAXBContext.newInstance(WeChatUnifiedOrderResponse.class);
        Unmarshaller unmarshaller = resJaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(responseText);
        WeChatUnifiedOrderResponse unifiedOrderResponse = (WeChatUnifiedOrderResponse) unmarshaller.unmarshal(reader);

        if("FAIL".equalsIgnoreCase(unifiedOrderResponse.getReturn_code())){
            throw new RuntimeException("get UnifiedOrder fail,msg:" + unifiedOrderResponse.getReturn_msg());
        }

        String checkSign = signService.unifiedPaySign(unifiedOrderResponse.toMap());
        if(!checkSign.equalsIgnoreCase(unifiedOrderResponse.getSign())){
            throw new RuntimeException("get UnifiedOrder fail,invalid sign");
        }

        return unifiedOrderResponse;
    }

    public WeChatCheckPaymentResponse checkAppPayment(WeChatPaymentCheckForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        form.setNonce_str(nonce_str);
        form.setAppid(params.getOpenAppId());
        form.setMch_id(params.getMchId());

        String sign = signService.unifiedPaySign(form.toMap());

        form.setSign(sign);

        JAXBContext jaxbContext = JAXBContext.newInstance(WeChatPaymentCheckForm.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(form, sw);
        String xml = sw.toString();

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(CHECK_PAYMENT);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);

        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");

        JAXBContext resJaxbContext = JAXBContext.newInstance(WeChatUnifiedOrderResponse.class);
        Unmarshaller unmarshaller = resJaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(responseText);
        WeChatCheckPaymentResponse checkPaymentResponse = (WeChatCheckPaymentResponse) unmarshaller.unmarshal(reader);

        if("FAIL".equalsIgnoreCase(checkPaymentResponse.getReturn_code())){
            throw new RuntimeException("get UnifiedOrder fail,msg:" + checkPaymentResponse.getReturn_msg());
        }
        return checkPaymentResponse;
    }

    public WeChatGiftMoneyResponse normalGiftMoney(WeChatGiftMoneyForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        form.setNonce_str(nonce_str);
        form.setMch_id(params.getMchId());
        form.setWxappid(params.getMpAppId());
        form.setSend_name(giftMoneySendName);
        form.setClient_ip(websiteIP);

        String sign = signService.unifiedPaySign(form.toMap());

        form.setSign(sign);
        //把参数转为xml
        JAXBContext jaxbContext = JAXBContext.newInstance(WeChatGiftMoneyForm.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(form, sw);
        String xml = sw.toString();
        //用xml格式参数去请求微信得到微信响应的结果

        HttpPost httpPost = new HttpPost(GIFTMONEY_URL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = giftClient.execute(httpPost);
        //拿出微信响应的结果xml转为java
        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");

        JAXBContext resJaxbContext = JAXBContext.newInstance(WeChatGiftMoneyResponse.class);
        Unmarshaller unmarshaller = resJaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(responseText);
        WeChatGiftMoneyResponse giftMoneyResponse = (WeChatGiftMoneyResponse) unmarshaller.unmarshal(reader);

        if("FAIL".equalsIgnoreCase(giftMoneyResponse.getReturn_code())){
            throw new RuntimeException("send giftMoney fail,msg:" + giftMoneyResponse.getReturn_msg());
        }else if("FAIL".equalsIgnoreCase(giftMoneyResponse.getResult_code())){
            throw new RuntimeException("send giftMoney fail,msg:" + giftMoneyResponse.getErr_code_des());
        }

        return giftMoneyResponse;
    }

    private HttpClient getHttpClientWithCa() throws Exception {
        if(Objects.nonNull(httpClientWithCa)){
            return httpClientWithCa;
        }else {
            synchronized (this){
                httpClientWithCa = new DefaultHttpClient();

                KeyStore keyStore = KeyStore.getInstance("PKCS12");

                FileInputStream inputStream = new FileInputStream(new File(caPath));
                try {
                    keyStore.load(inputStream, params.getMchId().toCharArray());
                } finally {
                    inputStream.close();
                }

                SSLContext sslcontext = SSLContexts
                        .custom()
                        .loadKeyMaterial(keyStore, params.getMchId().toCharArray())
                        .build();
                SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);
                Scheme sch = new Scheme("https", 8443, socketFactory);
                httpClientWithCa.getConnectionManager().getSchemeRegistry().register(sch);
                return httpClientWithCa;
            }
        }
    }
}
