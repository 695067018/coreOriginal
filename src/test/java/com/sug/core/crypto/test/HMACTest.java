package com.sug.core.crypto.test;

import com.sug.core.platform.crypto.HMAC;
import com.sug.core.util.EncodingUtils;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Greg.Chen on 2015/7/27.
 */
public class HMACTest {

    @Test
    public void test() throws Exception {
        String key = HMAC.initMacKey();

        System.out.println("key:" + key);
    }

    @Test
    public void test2()  throws Exception {
        String inputStr = "uri=/uj-oa-api/member/24&clientId=1fdbbac5f460635eb25854530a7a8cbe&method=GET&ascription=厦门&timestamp=1445938249344";
        System.err.println("原文:\n" + inputStr);
        String key = "Y9ek87ViyqClsAneJeEriW24ROpko1XwZd6hjxUcR/f7QFpzhO2qKK3SZH3y1Ax5hG+LXWnLIL7QDv8Ks+0stQ==";

        byte[] inputData = inputStr.getBytes(Charset.forName("UTF-8"));

        System.out.println("key:" + key);
        // 验证HMAC对于同一内容，同一密钥加密是否一致
        System.out.println("encrypt:" + EncodingUtils.encryptBASE64(HMAC.encryptHMAC(inputData, key)));

        assertArrayEquals(HMAC.encryptHMAC(inputData, key), HMAC.encryptHMAC(
                inputData, key));



    }

}
