package com.sug.core.util.jsonFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/22.
 */
public class SimpleDateTimeDeserializer extends JsonDeserializer<Date> {


    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return formatter.parse(jsonParser.getText());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public Date deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext,TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(jsonParser,deserializationContext);
    }
}
