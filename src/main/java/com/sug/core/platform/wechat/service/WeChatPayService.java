package com.sug.core.platform.wechat.service;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.wechat.constants.WeChatJsInter;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.*;
import com.sug.core.platform.wechat.request.WeChatPayNotifyForm;
import com.sug.core.platform.wechat.response.WeChatAppPayResponse;
import com.sug.core.platform.wechat.response.WeChatCheckPaymentResponse;
import com.sug.core.platform.wechat.response.WeChatJsPayResponse;
import com.sug.core.platform.wechat.response.WeChatUnifiedOrderResponse;
import com.sug.core.platform.wx.model.WxPrepayRequest;
import com.sug.core.platform.wx.model.WxPrepayResponse;
import com.sug.core.platform.wx.service.MD5Util;
import com.sug.core.util.RandomStringGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

@Service
public class WeChatPayService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayService.class);

    private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final String CHECK_PAYMENT = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Autowired
    private WeChatParams params;

    @Autowired
    private WeChatSignService signService;

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

        JAXBContext jaxbContext = JAXBContext.newInstance(WeChatUnifiedOrderForm.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(form, sw);
        String xml = sw.toString();

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(UNIFIEDORDER_URL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);

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
}
