package org.example.urlshortener.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();
    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream( "config.properties")) {
            if (input == null) {
                System.out.println("config.properties Not found");

            }
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Caught error ):");
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }


    public static void main(String[] args) {
        String dbUrl = AppConfig.getProperty("database.url");
        String appVersion = AppConfig.getProperty("application.version");
        System.out.println("Database URL: " + dbUrl);
        System.out.println("Application Version: " + appVersion);
    }
}