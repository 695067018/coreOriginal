package com.sug.core.platform.wechat.service;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.wechat.constants.WeChatParams;
import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.WeChatPaySignForm;
import com.sug.core.platform.wx.service.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WeChatSignService {

    @Autowired
    private WeChatParams params;

    public String unifiedPaySign(SortedMap<String, String> map) {
        return signature(map);
    }

    public String jsPaySign(WeChatPaySignForm form){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appId",params.getAppId());
        map.put("timeStamp",form.getTimeStamp());
        map.put("nonceStr",form.getNonceStr());
        map.put("signType",WeChatPayConstants.SIGNTYPE_JSPAY);
        map.put("package",form.getPackageBody());

        return signature(map);
    }

    private String signature(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();

        for ( Map.Entry entry: packageParams.entrySet()) {
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");
            }
        }
        sb.append("key=");
        sb.append(params.getApiSecret());

        return MD5Util.MD5Encode(sb.toString(),"UTF-8")
                .toUpperCase();
    }
}
