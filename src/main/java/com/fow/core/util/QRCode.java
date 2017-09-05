package com.fow.core.util;

/**
 * Created by suggestion on 2015/4/14.
 */
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;


/**
 * Created by suggestion on 2015/4/10.
 *
 * 生成二维码
 *
 */
public class QRCode {

    /**
     * 生成二维码方法
     * @param  contents 将contents的内容生成到二维码中
     * @param  outputStream 将生成的二维码输出到outputStream
     * @throws Exception
     */
    public static void Encoder(String contents,OutputStream outputStream){


        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();

        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = null;

        try {

            matrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.QR_CODE, 200, 200, hints);

        } catch (WriterException e) {

            e.printStackTrace();

        }


        try {

            MatrixToImageWriter.writeToStream(matrix, "png", outputStream);

        } catch (IOException e) {

            e.printStackTrace();

        }


    }
}

