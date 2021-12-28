package ru.job4j.dream;

import java.io.InputStream;
import java.util.Properties;

public class GetSettings {

    public static String getProperty(String param) {
        return loadProperties().getProperty(param);
    }

    private static Properties loadProperties() {
        var properties = new Properties();
        try (InputStream in = GetSettings.class.getClassLoader().getResourceAsStream("Application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return properties;
    }
}