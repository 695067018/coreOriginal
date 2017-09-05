package com.fow.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016-10-18.
 */
public class SimpleFormatUtils {
    public static String simpleDataFormat(Date date, String regex){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regex);
        return simpleDateFormat.format(date);
    }
}
