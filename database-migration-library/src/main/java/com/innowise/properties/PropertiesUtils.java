package com.innowise.properties;

import com.innowise.constants.ResourcesConstants;
import com.innowise.exception.PropertiesUtilsException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for working with file properties
 * The class implements the Singleton pattern and allows loading
 * properties from a file and retrieving their values by key
 */

@Slf4j
public class PropertiesUtils {

    private static PropertiesUtils instance;
    private Properties properties;

    private PropertiesUtils() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Retrieves the singleton instance of the {@code PropertiesUtils} class
     * @return the singleton instance of the {@code PropertiesUtils} class
     */
    public static PropertiesUtils getInstance() {
        if (instance == null) {
            instance = new PropertiesUtils();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream inputStream = PropertiesUtils.class
                .getClassLoader()
                .getResourceAsStream(ResourcesConstants.RESOURCE_FILE_NAME)) {

            if (inputStream == null) {
                log.error("Unable to find resource: {}", ResourcesConstants.RESOURCE_FILE_NAME);
                throw new PropertiesUtilsException("Resource not found");
            }

            properties.load(inputStream);
            log.info("Properties successfully loaded from file: {}", ResourcesConstants.RESOURCE_FILE_NAME);
        } catch (IOException e) {
            log.error("Error loading properties file: {}", ResourcesConstants.RESOURCE_FILE_NAME, e);
            throw new PropertiesUtilsException("Error loading properties file", e);
        }
    }

    /**
     * Retrieves the value of a property by the specified key
     * @param key the property key
     * @return the property value or {@code null} if the key is not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}