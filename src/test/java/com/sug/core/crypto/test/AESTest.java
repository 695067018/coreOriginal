package com.sug.core.crypto.test;

import com.sug.core.platform.crypto.AES;
import com.sug.core.util.EncodingUtils;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2015/10/23.
 */
public class AESTest {

    @Test
    public void init() throws Exception {
        String key = AES.initKey(null);

        System.out.println("密钥:" + key);
    }



    @Test
    public void test() throws Exception {
        String inputStr = "1";
        String key = AES.initKey(null);
        System.err.println("原文:\t" + inputStr);

        System.err.println("密钥:\t" + key);

        byte[] inputData = inputStr.getBytes();
        inputData = AES.encrypt(inputData, key);

        System.err.println("加密后:\t" + EncodingUtils.encryptBASE64(inputData));

        byte[] outputData = AES.decrypt(inputData, key);
        String outputStr = new String(outputData);

        System.err.println("解密后:\t" + outputStr);

        Assert.assertEquals(inputStr, outputStr);
    }

    @Test
    public void decrypt() throws Exception {
        String key = "pE3CffllQQP2Ay40Z3PBSA==";
        byte[] outputData = AES.decrypt(EncodingUtils.decryptBASE64("7dKPCPElc/QYE2GEOv6Z3Q=="), key);

        String outputStr = new String(outputData);

        System.err.println("解密后:\t" + outputStr);
    }

}
