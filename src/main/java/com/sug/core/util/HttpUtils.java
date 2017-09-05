package com.sug.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Greg.Chen on 14-11-12.
 */
public class HttpUtils {

    /**
     * 返回站点路径
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {

        if(request.getServerPort() == 80)
        {
            return request.getScheme() + "://" + request.getServerName()  + request.getContextPath();
        }

        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
