package com.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
public class Util {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object o) throws Exception {
        return mapper.writeValueAsString(o);
    }

    public static <T> T parse(String string, Class<T> clazz) throws Exception {
        return mapper.readValue(string, clazz);
    }

    public static <T> T parse(String string, TypeReference<T> typeReference) throws Exception {
        return mapper.readValue(string, typeReference);
    }
}
