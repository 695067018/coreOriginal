package com.fow.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Created by greg.chen on 14-10-23.
 */
@Component
public class MessageSourceUtils {

    @Autowired
    private MessageSource resources;

    public String getMessage(String key){
        if(!StringUtils.hasText(key))
            return null;

        return resources.getMessage(key, null, "Default", null);
    }


}
