package com.fow.core.util;

import java.util.Random;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:18
 */
public class RandomStringGenerator {

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        return getRandomString(base,length);
    }

    /**
     * 获取一定长度的随机数字字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomNumberStringByLength(int length){
        String base = "0123456789";
        return getRandomString(base,length);
    }

    private static String getRandomString(String base,int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
