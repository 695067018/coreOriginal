package com.fow.core.platform.wx.service;

import com.fow.core.platform.crypto.SHA1;
import com.fow.core.platform.wx.model.WxConfig;
import com.fow.core.platform.wx.model.WxGetAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Greg.Chen on 2015/6/2.
 */
@Service
public class WxAuthService {
    private static final Logger log = LoggerFactory.getLogger(WxAuthService.class);

    @Autowired
    private WxConfig wxConfig;

    public boolean validateAuth(WxGetAuth auth){

        String excepted = SHA1.encrypt(auth.getStringToHash(wxConfig.getToken()));

        if (auth.getSignature() == null || !auth.getSignature().equals(excepted)) {
            log.error("Authentication failed! excepted echostr ->" + excepted);
            log.error("                                 actual ->" + auth.getSignature());
            return false;
        }

        return true;
    }

}
