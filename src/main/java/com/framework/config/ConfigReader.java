package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration access.
 * Resolution order (highest wins): JVM system property (-Dkey=value)
 *   -> environment variable (key upper-cased, '.' -> '_', e.g. REQRES_API_KEY)
 *   -> config.properties on the classpath.
 * This lets the same code run locally, on Jenkins and on GitHub Actions without edits.
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() { }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (isBlank(value)) {
            value = PROPS.getProperty(key);
        }
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration key: " + key);
        }
        return value.trim();
    }

    public static String get(String key, String defaultValue) {
        try {
            return get(key);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, "false"));
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
