package com.fow.core.platform.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2014/10/20.
 */
public class MD5 {
    public static Logger logger = LoggerFactory.getLogger(MD5.class);

    public static String encrypt(String str) {

        // 返回字符串
        String md5Str = null;
        // 操作字符串
        StringBuffer buf = new StringBuffer();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }

        // 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
        md.update(str.getBytes());
        // 计算出摘要,完成哈希计算。
        byte b[] = md.digest();
        int i;
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
            buf.append(Integer.toHexString(i));
        }
        // 32位的加密
        md5Str = buf.toString();


        return md5Str;
    }

}
