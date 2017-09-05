package com.fow.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greg.Chen on 2015/1/28.
 */
public class SequenceNumUtils {

    /*
* 同一微秒提交订单重复的概率为1%
* */
    public static String generateNum(){
        int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
        int r2=(int)(Math.random()*(10));
        String date = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        String orderId = date + String.valueOf(r1)+String.valueOf(r2);// 订单ID
        return orderId;
    }

    public static String generateShortNum(){
        int r1 = (int)(Math.random()*(10));
        String date = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        String cellaretteId = date + String.valueOf(r1);
        return cellaretteId;
    }
}
