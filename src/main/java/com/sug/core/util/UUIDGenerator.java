package com.sug.core.util;

import java.util.Random;
import java.util.UUID;

public class UUIDGenerator {
    public static String generateByString(String source){
        int first = new Random(10).nextInt(8) + 1;
        int hashCodeV = UUID.fromString(source).toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return first + String.format("%015d", hashCodeV);
    }
}
