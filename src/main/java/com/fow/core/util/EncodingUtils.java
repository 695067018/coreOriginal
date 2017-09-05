package com.fow.core.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Greg.Chen on 2015/5/26.
 */
public class EncodingUtils {
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key.getBytes(CharacterEncodings.CHARSET_UTF_8));
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return new String(Base64.encodeBase64(key), CharacterEncodings.CHARSET_UTF_8);
    }

}
