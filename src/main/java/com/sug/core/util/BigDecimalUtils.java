package com.sug.core.util;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static BigDecimal add(BigDecimal value1,BigDecimal value2){
        return value1.add(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal subtract(BigDecimal value1,BigDecimal value2){
        return value1.subtract(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal multiply(BigDecimal value1,BigDecimal value2){
        return value1.multiply(value2).setScale(2,BigDecimal.ROUND_UP);
    }

    public static BigDecimal multiply(BigDecimal value1,Integer value2){
        return value1.multiply(new BigDecimal(value2)).setScale(2,BigDecimal.ROUND_UP);
    }
}
