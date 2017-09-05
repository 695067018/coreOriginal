package com.sug.core.platform.web.rest.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by greg.chen on 14-10-15.
 */
public class ExceptionUtils {

    public static String stackTrace(Throwable e){
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }

}
