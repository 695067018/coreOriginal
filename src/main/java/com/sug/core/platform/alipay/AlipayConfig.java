package com.sug.core.platform.alipay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Greg.chen on 2016-07-04.
 */
@Component
public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串

    @Value("${ali.partner}")
    public String partner;


    @Value("${ali.publicKey}")
    public String ali_public_key;


}
