package com.fow.core.util.test;

import com.fow.core.util.Convert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator on 2014/10/17.
 */
public class ConvertTest {

    @Test
    public void dateToStringTest(){

        System.out.println(Convert.toString(new Date(), "MM/dd/yyyy'T'HH:mm:ss"));

    }

}
