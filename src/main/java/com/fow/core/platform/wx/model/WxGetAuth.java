package com.fow.core.platform.wx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Greg.Chen on 2015/6/2.
 */
public class WxGetAuth {

    public WxGetAuth(String signature, String timestamp, String nonce, String echostr) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.echostr = echostr;
    }

    String signature;
    String timestamp;
    String nonce;
    String echostr;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }

    @Override
    public String toString() {
        return "WxGetAuth{" +
                "signature='" + signature + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", nonce='" + nonce + '\'' +
                ", echostr='" + echostr + '\'' +
                '}';
    }

    public String getStringToHash(String token) {
        List<String> list = new ArrayList<String>();
        list.add(timestamp);
        list.add(nonce);
        list.add(token);

        String result = "";
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            result += list.get(i);
        }
        return result;
    }
}
