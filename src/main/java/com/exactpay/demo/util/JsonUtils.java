package com.exactpay.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Utility class for JSON operations using Jackson ObjectMapper.
 * Provides methods for serialization and deserialization of JSON.
 */
@Component
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Configure ObjectMapper with common settings
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Get the configured ObjectMapper instance.
     *
     * @return the ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Convert an object to JSON string.
     *
     * @param object the object to convert
     * @return JSON string representation of the object
     * @throws JsonProcessingException if conversion fails
     */
    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Convert a JSON string to an object of the specified class.
     *
     * @param json  the JSON string
     * @param clazz the class to convert to
     * @param <T>   the type of the class
     * @return the converted object
     * @throws IOException if conversion fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    /**
     * Convert a JSON string to an object using a TypeReference.
     * Useful for generic types like List<T> or Map<K,V>.
     *
     * @param json          the JSON string
     * @param typeReference the TypeReference describing the target type
     * @param <T>           the type to convert to
     * @return the converted object
     * @throws IOException if conversion fails
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(json, typeReference);
    }

    /**
     * Convert an object to a Map.
     *
     * @param object the object to convert
     * @return a Map representation of the object
     */
    public static Map<String, Object> toMap(Object object) {
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Convert a Map to an object of the specified class.
     *
     * @param map   the map to convert
     * @param clazz the class to convert to
     * @param <T>   the type of the class
     * @return the converted object
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}