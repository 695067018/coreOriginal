package com.sug.core.platform.web.rest;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.util.HttpUtils;
import com.sug.core.platform.exception.ForbiddenException;
import com.sug.core.platform.exception.UserAuthorizationException;
import com.sug.core.platform.web.request.RemoteAddress;
import com.sug.core.platform.web.rest.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Greg.Chen on 2015/5/7.
 */
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    private final static String DOMAIN = "domain";
    private final static String REDIRECT_DOMAIN = "redirect_domain";

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException() {

        return "redirect:/404";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException() {

        return "redirect:/404";
    }

    @ExceptionHandler(UserAuthorizationException.class)
    public String handleUserAuthorizationException(HttpServletRequest request) throws UnsupportedEncodingException {

        RemoteAddress address = RemoteAddress.create(request);

        String domain = (String)request.getAttribute(DOMAIN);

        String returnUrl = (domain != null ? HttpUtils.getBasePath(request) : "") + request.getServletPath();

        String url = String.format("redirect:%s/login?returnUrl=%s", domain != null ? domain : "", URLEncoder.encode(returnUrl,"UTF-8"));

        return url;
    }

    @ExceptionHandler(value = Throwable.class)
    public View error(Throwable e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);

        RedirectView view = new RedirectView("/500");

        return view;
    }
}
