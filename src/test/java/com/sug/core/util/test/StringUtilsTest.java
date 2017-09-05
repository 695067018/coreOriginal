package com.sug.core.util.test;

import com.sug.core.util.StringUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by greg.chen on 14-10-13.
 */
public class StringUtilsTest {

    @Test
    public void hasText(){
        String s = null;
        Assert.assertTrue(!StringUtils.hasText(s));
    }

    @Test
    public void test1(){

        int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
        int r2=(int)(Math.random()*(10));
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String id =date + String.valueOf(r1);// 订单ID


       System.out.print(id);
    }

}
