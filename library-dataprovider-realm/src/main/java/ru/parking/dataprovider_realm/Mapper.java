package ru.parking.dataprovider_realm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public final class Mapper {
    private static ObjectMapper MAPPER;

    public static ObjectMapper get() {
        if (MAPPER == null) {
            MAPPER = new ObjectMapper();

            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.setVisibilityChecker(MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        }

        return MAPPER;
    }

    public static String string(Object data) {
        try {
            return get().writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectOrThrow(String data, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
        return get().readValue(data, type);
    }

    public static <T> T objectOrThrow(String data, JavaType type) throws JsonParseException, JsonMappingException, IOException {
        return get().readValue(data, type);
    }

    public static <T> T objectOrThrow(String data, TypeReference type) throws JsonParseException, JsonMappingException, IOException {
        return (T) get().readValue(data, type);
    }

    public static <T> T object(String data, Class<T> type) {
        try {
            return objectOrThrow(data, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
