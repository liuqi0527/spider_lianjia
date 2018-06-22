package com.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author LiuQi - [Created on 2018-03-01]
 */
public class Util {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private static DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public static String toYearMonth(LocalDate localDate) {
        return dateTimeFormatter.format(localDate);
    }

    public static LocalDate fromYearMonth(String dateText) {
        if (dateText.length() == 7) {
            dateText = dateText + "-01";
        }
        return LocalDate.parse(dateText, dateTimeFormatter1);
    }
}
