package com.sug.core.platform.wechat.twoMch;

import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.*;
import com.sug.core.platform.wechat.response.WeChatAppPayResponse;
import com.sug.core.platform.wechat.response.WeChatJsPayResponse;
import com.sug.core.platform.wechat.response.WeChatUnifiedOrderResponse;
import com.sug.core.util.RandomStringGenerator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

import static com.sug.core.platform.wechat.constants.WeChatPayConstants.TRADETYPE_JS;

@Service("twoMchWeChatPayService")
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
        unifiedOrderForm.setAppid(params.getMpAppId());
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setTrade_type(TRADETYPE_JS);
        unifiedOrderForm.setMch_id(params.getMpMchId());

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
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_NATIVE);

        //generate unified order
        return this.generateUnifiedorder(unifiedOrderForm).getCode_url();
    }

    public WeChatAppPayResponse getAppPayParams(WeChatAppPayParamsForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        Long timestamp = System.currentTimeMillis();

        WeChatUnifiedOrderForm unifiedOrderForm = new WeChatUnifiedOrderForm();
        BeanUtils.copyProperties(form,unifiedOrderForm);
        unifiedOrderForm.setAppid(params.getOpenAppId());
        unifiedOrderForm.setTrade_type(WeChatPayConstants.TRADETYPE_APP);
        unifiedOrderForm.setMch_id(params.getOpenMchId());

        //generate unified order
        String prepayId = this.generateUnifiedorder(unifiedOrderForm).getPrepay_id();

        WeChatAppPayResponse response = new WeChatAppPayResponse();
        response.setAppid(params.getOpenAppId());
        response.setPartnerid(params.getOpenMchId());
        response.setPrepayid(prepayId);
        response.setPackageBody("Sign=WXPay");
        response.setNoncestr(nonce_str);
        response.setTimestamp(timestamp.toString());

        WeChatPaySignForm signForm = new WeChatPaySignForm();
        BeanUtils.copyProperties(response,signForm);
        response.setSign(signService.appPaySign(signForm));

        return response;
    }

    private WeChatUnifiedOrderResponse generateUnifiedorder(WeChatUnifiedOrderForm form) throws Exception {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(15);
        form.setNonce_str(nonce_str);
        form.setNotify_url(params.getNotifyUrl());

        String sign = signService.unifiedPaySign(form.toMap(),form.getTrade_type().equalsIgnoreCase(TRADETYPE_JS) ? 1 : 2);

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

        String checkSign = signService.unifiedPaySign(unifiedOrderResponse.toMap(),form.getTrade_type().equalsIgnoreCase(TRADETYPE_JS) ? 1 : 2);
        if(!checkSign.equalsIgnoreCase(unifiedOrderResponse.getSign())){
            throw new RuntimeException("get UnifiedOrder fail,invalid sign");
        }

        return unifiedOrderResponse;
    }
}
