package com.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() { }

    public static List<Map<String, String>> readList(String classpathFile) {
        try (InputStream in = JsonUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("JSON file not found on classpath: " + classpathFile);
            }
            return MAPPER.readValue(in, new TypeReference<>() { });
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse JSON file " + classpathFile, e);
        }
    }
}
