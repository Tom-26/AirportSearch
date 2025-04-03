package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static ObjectMapper get() {
        return mapper;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectWriter writer = mapper.writer();
        return writer.writeValueAsString(obj);
    }
}
