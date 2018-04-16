package com.sug.core.platform.wechat.twoMch;

import com.sug.core.platform.wechat.constants.WeChatPayConstants;
import com.sug.core.platform.wechat.form.WeChatPaySignForm;
import com.sug.core.platform.wx.service.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service("twoMchWeChatSignService")
public class WeChatSignService {

    @Autowired
    private WeChatParams params;

    public String unifiedPaySign(SortedMap<String, String> map,Integer type) {
        return signature(map,type);
    }

    public String jsPaySign(WeChatPaySignForm form){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appId",params.getMpAppId());
        map.put("timeStamp",form.getTimeStamp());
        map.put("nonceStr",form.getNonceStr());
        map.put("signType",WeChatPayConstants.SIGNTYPE_JSPAY);
        map.put("package",form.getPackageBody());

        return signature(map,1);
    }

    public String appPaySign(WeChatPaySignForm form){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("appid",params.getOpenAppId());
        map.put("partnerid",params.getOpenMchId());
        map.put("prepayid",form.getPrepayid());
        map.put("nonceStr",form.getNonceStr());
        map.put("timestamp",form.getTimeStamp());
        map.put("package",form.getPackageBody());

        return signature(map,2);
    }

    private String signature(SortedMap<String, String> packageParams,Integer type) {
        StringBuffer sb = new StringBuffer();

        for ( Map.Entry entry: packageParams.entrySet()) {
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (StringUtils.hasText(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");
            }
        }
        sb.append("key=");
        sb.append(type == 1 ? params.getMpApiSecret() : params.getOpenApiSecret());

        return MD5Util.MD5Encode(sb.toString(),"UTF-8")
                .toUpperCase();
    }
}
