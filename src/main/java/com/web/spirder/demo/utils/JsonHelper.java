package com.web.spirder.demo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hezifeng
 * @create 2023/3/31 15:17
 */
public class JsonHelper {
    private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);
    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPERS = ThreadLocal.withInitial(() -> {
        return createObjectMapper();
    });

    public JsonHelper() {
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        om.registerModule(javaTimeModule);
        om.registerModule(new Jdk8Module());
        om.registerModule(new IEnumModule());
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return om;
    }

    public static String stringify(Object object) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).writeValueAsString(object);
        } catch (IOException var2) {
            throw new RuntimeException(var2.getMessage(), var2);
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(clazz).readValue(json);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static <T> T parseOrNull(String json, Class<T> clazz) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(clazz).readValue(json);
        } catch (IOException var3) {
            log.warn("解析JSON出现错误：" + var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T parse(JsonNode jsonNode, Class<T> clazz) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(clazz).readValue(jsonNode);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static <T> T parse(byte[] json, Class<T> clazz) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(clazz).readValue(json);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static <T> T parse(String json, TypeReference<T> typeReference) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(typeReference).readValue(json);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static Object parseObject(byte[] data, TypeReference<?> typeReference) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readerFor(typeReference).readValue(data);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static JsonNode parse(String json) {
        try {
            return ((ObjectMapper)OBJECT_MAPPERS.get()).readTree(json);
        } catch (IOException var2) {
            throw new RuntimeException(var2.getMessage(), var2);
        }
    }

    public static <T> T parse(String json, JavaType type) {
        try {
            ObjectMapper objectMapper = (ObjectMapper)OBJECT_MAPPERS.get();
            return objectMapper.readerFor(type).readValue(json);
        } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage(), var3);
        }
    }

    public static JavaType constructJavaType(TypeReference<?> typeReference) {
        return ((ObjectMapper)OBJECT_MAPPERS.get()).getTypeFactory().constructType(typeReference);
    }
}
