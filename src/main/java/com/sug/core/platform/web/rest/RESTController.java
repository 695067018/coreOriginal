package com.sug.core.platform.web.rest;

import com.sug.core.platform.exception.*;
import com.sug.core.platform.web.rest.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.SocketTimeoutException;

/**
 * Created by greg.chen on 14-10-13.
 */
public class RESTController{

    private static final Logger logger = LoggerFactory.getLogger(RESTController.class);

    @Autowired
    protected ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse error(Throwable e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);
        return errorResponseBuilder.createErrorResponse(e, false);
    }

    @ExceptionHandler(value = SocketTimeoutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse socketTimeout(SocketTimeoutException e) {
        String errorMessage = ExceptionUtils.stackTrace(e);
        logger.error(errorMessage);

        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(APIErrorCode.SOCKET_TIMEOUT.getCode());
        response.setMessage(e.getMessage());


        return errorResponseBuilder.createErrorResponse(e, false);
    }


    @ExceptionHandler(value=ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse resourceNotFound(ResourceNotFoundException ex){
        return errorResponseBuilder.createErrorResponse(ex,true);
    }

    @ExceptionHandler(value=UserAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse unauthorized(UserAuthorizationException ex){
        return errorResponseBuilder.createErrorResponse(ex,true);
    }

    @ExceptionHandler(value=ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse forbidden(ForbiddenException ex){
        ErrorResponse response = errorResponseBuilder.createErrorResponse(ex,true);
        response.setErrorCode(APIErrorCode.PERMISSION_REQUIRED.getCode());
        return errorResponseBuilder.createErrorResponse(ex,true);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse validationError(InvalidRequestException e) {
        ErrorResponse response = errorResponseBuilder.createValidationResponse(e);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse validationError(MethodArgumentNotValidException e) {
        return errorResponseBuilder.createValidationResponse(e);
    }


    @ExceptionHandler(value=SessionExpiredException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse sessionExpired(SessionExpiredException ex){
        ErrorResponse response = errorResponseBuilder.createErrorResponse(ex,true);
        response.setErrorCode(APIErrorCode.SESSION_EXPIRED.getCode());
        return response;
    }

    @ExceptionHandler(value=VisitorNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse visitorNotFound(VisitorNotFoundException ex){
        ErrorResponse response = errorResponseBuilder.createErrorResponse(ex,true);
        response.setErrorCode(APIErrorCode.VisitorNotFound.getCode());
        return response;
    }

    @ExceptionHandler(value=LoginRequiredException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse loginRequired(LoginRequiredException ex){
        ErrorResponse response = errorResponseBuilder.createValidationResponse(ex);
        response.setErrorCode(APIErrorCode.LOGIN_REQUIRED.getCode());
        return response;
    }
}
