package com.sug.core.platform.web.rest.exception;


import com.sug.core.platform.exception.LoginRequiredException;
import com.sug.core.platform.log.ActionLog;
import com.sug.core.platform.log.ActionLogger;
import com.sug.core.platform.log.ActionResult;
import com.sug.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.sug.core.platform.web.rest.runtime.RuntimeSettings;
import com.sug.core.platform.web.rest.runtime.RuntimeEnvironment;

import java.util.List;


@Component
public class ErrorResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ErrorResponseBuilder.class);

    @Autowired
    private RuntimeSettings runtimeSettings;

    public ErrorResponse createErrorResponse(Throwable e, boolean isLog) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        if(isLog)
            logger.error(errorMessage);

        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setExceptionClass(e.getClass().getName());

       // error.setRequestId(requestContext.getRequestId());
        if (!RuntimeEnvironment.prod.equals(runtimeSettings.getEnvironment())) {
            error.setExceptionTrace(errorMessage);
        }

        return error;
    }

    public ErrorResponse createValidationResponse(LoginRequiredException e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);

        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(StringUtils.hasText(e.getCode())?e.getCode():APIErrorCode.LOGIN_REQUIRED.getCode());
        response.setMessage(e.getMessage());

        return response;
    }

    public ErrorResponse createValidationResponse(InvalidRequestException e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);

        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(StringUtils.hasText(e.getCode())?e.getCode():APIErrorCode.VALIDATION_ERROR.getCode());
        response.setMessage(e.getMessage());

        return response;
    }

    public ErrorResponse createValidationResponse(MethodArgumentNotValidException e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);

        ErrorResponse response = new ErrorResponse();

        response.setErrorCode(APIErrorCode.VALIDATION_ERROR.getCode());
        List<org.springframework.validation.FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        org.springframework.validation.FieldError fieldError = fieldErrors.get(0);
        FieldError error = new FieldError();
        error.setField(fieldError.getField());
        error.setMessage(fieldError.getDefaultMessage());
        response.setFieldError(error);

        return response;
    }

}
