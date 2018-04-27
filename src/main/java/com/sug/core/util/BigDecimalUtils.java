package com.sug.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class BigDecimalUtils {
    private final static Logger logger = LoggerFactory.getLogger(BigDecimalUtils.class);

    public static BigDecimal add(BigDecimal value1,BigDecimal value2){
        return value1.add(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal subtract(BigDecimal value1,BigDecimal value2){
        return value1.subtract(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal subtract(BigDecimal value1,BigDecimal value2,int scale){
        return value1.subtract(value2).setScale(scale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal value1,BigDecimal value2){
        return value1.multiply(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal multiply(BigDecimal value1,BigDecimal value2,int scale){
        return  value1.multiply(value2).setScale(scale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal value1,Integer value2){
        return value1.multiply(new BigDecimal(value2)).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal divide(Integer value1,Integer value2){
        return new BigDecimal(value1).divide(new BigDecimal(value2),2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_UP);
    }
}
