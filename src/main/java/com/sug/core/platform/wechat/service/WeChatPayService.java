package com.sug.core.platform.wechat.service;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.wechat.constants.WeChatJsInter;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.WeChatJsPayParamsForm;
import com.sug.core.platform.wechat.form.WeChatNativeParamsForm;
import com.sug.core.platform.wechat.form.WeChatPaySignForm;
import com.sug.core.platform.wechat.form.WeChatUnifiedOrderForm;
import com.sug.core.platform.wechat.response.WeChatJsPayResponse;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class WeChatPayService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayService.class);

    private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    @Autowired
    private WeChatParams params;

    @Autowired
    private WeChatSignService signService;

    public WeChatJsPayResponse getJsPayParams(WeChatJsPayParamsForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        Long timestamp = System.currentTimeMillis();

        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setCreateIP(form.getClientIP());
        unifiedOrderForm.setTradeType(WeChatPayConstants.TRADETYPE_JS);

        //generate unified order
        String packageBody = "prepay_id=" + this.generateUnifiedorder(unifiedOrderForm);

        WeChatPaySignForm signForm = new WeChatPaySignForm();
        signForm.setTimeStamp(timestamp.toString());
        signForm.setNonceStr(nonce_str);
        signForm.setPackageBody(packageBody);

        WeChatJsPayResponse response = new WeChatJsPayResponse();
        BeanUtils.copyProperties(signForm,response);
        response.setSignType(WeChatPayConstants.SIGNTYPE_JSPAY);
        response.setPaySign(signService.jsPaySign(signForm));

        return response;
    }

    public String getQrCodeUrl(WeChatNativeParamsForm form) throws Exception {
        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setCreateIP(form.getApiIP());
        unifiedOrderForm.setTradeType(WeChatPayConstants.TRADETYPE_JS);

        //generate unified order
        return this.generateUnifiedorder(unifiedOrderForm);
    }

    private String generateUnifiedorder(WeChatUnifiedOrderForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        int total_fee = (int) (form.getFee() * 100);

        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        xml.append(String.format("<appid>%s</appid>",params.getAppId()));
        xml.append(String.format("<mch_id>%s</mch_id>",params.getMchId()));
        xml.append(String.format("<nonce_str>%s</nonce_str>",nonce_str));
        xml.append(String.format("<body><![CDATA[%s]]></nonce_str>",form.getBody()));
        xml.append(String.format("<out_trade_no>%s</out_trade_no>",form.getPaymentNum()));
        xml.append(String.format("<total_fee>%s</total_fee>",total_fee));
        xml.append(String.format("<spbill_create_ip>%s</spbill_create_ip>",form.getCreateIP()));
        xml.append(String.format("<notify_url>%s</notify_url>",params.getNotifyUrl()));
        xml.append(String.format("<trade_type>%s</trade_type>",form.getTradeType()));

        if(form.getTradeType().equalsIgnoreCase(WeChatPayConstants.TRADETYPE_JS)){
            if(StringUtils.isEmpty(form.getOpenId())){
                throw new ResourceNotFoundException("require open id");
            }
            xml.append(String.format("<openid>%s</openid>",form.getOpenId()));
        }

        if(form.getTradeType().equalsIgnoreCase(WeChatPayConstants.TRADETYPE_NATIVE)){
            if(StringUtils.isEmpty(form.getOpenId())){
                throw new ResourceNotFoundException("require product id");
            }
            xml.append(String.format("<product_id>%s</product_id>",form.getProductId()));
        }

        WeChatPaySignForm signForm = new WeChatPaySignForm();
        BeanUtils.copyProperties(form,signForm);
        signForm.setNonceStr(nonce_str);
        signForm.setTotalFee(total_fee);

        String sign = signService.unifiedPaySign(signForm);
        xml.append(String.format("<sign>%s</sign>",sign));

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(UNIFIEDORDER_URL);
        httpPost.setEntity(new StringEntity(xml.toString(), "UTF-8"));
        HttpResponse response = client.execute(httpPost);

        byte[] content = EntityUtils.toByteArray(response.getEntity());
        String responseText = new String(content, "UTF-8");

        Map map = doXMLParse(responseText);
        if("FAIL".equalsIgnoreCase(map.get("return_code").toString())){
            throw new RuntimeException("get UnifiedOrder fail,msg:" + map.get("return_msg").toString());
        }
        return map.get("prepay_id").toString();
    }

    private Map<String, String> doXMLParse(String strxml) throws Exception {
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

    private static String getChildrenText(List children) {
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


}
