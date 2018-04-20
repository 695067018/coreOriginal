package com.sug.core.util.jsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by A on 2014/12/30.
 */
public class SimpleBigDecimalSerializer extends JsonSerializer<BigDecimal>{

    public static DecimalFormat format = new DecimalFormat("#0.0000");

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        if(null == value){
            jgen.writeNull();
        } else{
            String output = format.format(value.doubleValue());
            jgen.writeNumber(output);
        }
    }
}
