package com.sug.core.platform.web.rest.exception;


import com.sug.core.platform.exception.LoginRequiredException;
import com.sug.core.platform.web.rest.runtime.RuntimeEnvironment;
import com.sug.core.platform.web.rest.runtime.RuntimeSettings;
import com.sug.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;


@Component
public class SimpleErrorResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SimpleErrorResponseBuilder.class);

    public SimpleErrorResponse createErrorResponse(Throwable e, boolean isLog) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        if(isLog)
            logger.error(errorMessage);

        SimpleErrorResponse error = new SimpleErrorResponse();
        error.setMessage(e.getMessage());
        return error;
    }

    public SimpleErrorResponse createValidationResponse(MethodArgumentNotValidException e,boolean isLog) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        if(isLog)
            logger.error(errorMessage);

        SimpleErrorResponse response = new SimpleErrorResponse();

        List<org.springframework.validation.FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        org.springframework.validation.FieldError fieldError = fieldErrors.get(0);
        response.setField(fieldError.getField());
        response.setMessage(fieldError.getDefaultMessage());

        return response;
    }

}
