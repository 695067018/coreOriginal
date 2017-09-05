package com.sug.core.crypto.test;

import com.sug.core.platform.crypto.RSA;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Greg.Chen on 2015/5/26.
 */
public class RSATest {
    RSA rsa;

    @Before
    public void setUp() {
        rsa = new RSA();

    }

    @Test
    public void test() throws Exception {
        rsa.init();
        System.err.println("公钥: \n\r" + rsa.getPublicKey());
        System.err.println("私钥： \n\r" + rsa.getPrivateKey());

        System.err.println("公钥加密——私钥解密");

        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        //公钥加密
        byte[] encodeData = rsa.encryptByPublicKey(data, rsa.getPublicKey());
        //私钥解密
        byte[] decodeData = rsa.decryptByPrivateKey(encodeData, rsa.getPrivateKey());

        String outputStr = new String(decodeData);

        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
    }

    @Test
    public void testSign() throws Exception {
        rsa.init();
        System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        byte[] encodedData = rsa.encryptByPrivateKey(data, rsa.getPrivateKey());
        byte[] decodedData = rsa.decryptByPublicKey(encodedData, rsa.getPublicKey());

        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        System.err.println("私钥签名——公钥验证签名");

        String sign = rsa.sign(encodedData, rsa.getPrivateKey());
        System.err.println("签名:\r" + sign);

        // 验证签名
        boolean status = rsa.verify(encodedData, rsa.getPublicKey(), sign);
        System.err.println("状态:\r" + status);

    }

}