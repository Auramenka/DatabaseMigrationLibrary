package com.innowise;

import com.innowise.connection.ConnectionManager;
import com.innowise.constants.ResourcesConstants;
import com.innowise.constants.DatabaseConfig;
import com.innowise.exception.MigrationException;
import com.innowise.properties.PropertiesUtils;
import com.innowise.service.MigrationFileReader;
import com.innowise.service.MigrationManager;
import com.innowise.service.MigrationTool;

public class Main {

    public static void main(String[] args) {

        try {
            PropertiesUtils propertiesUtils = new PropertiesUtils(ResourcesConstants.RESOURCE_FILE_NAME);
            String url = propertiesUtils.getProperty(DatabaseConfig.DB_URL);
            String user = propertiesUtils.getProperty(DatabaseConfig.DB_USERNAME);
            String password = propertiesUtils.getProperty(DatabaseConfig.DB_PASSWORD);

            ConnectionManager connectionManager = new ConnectionManager(url, user, password);
            MigrationFileReader migrationFileReader = new MigrationFileReader();
            MigrationManager migrationManager = new MigrationManager(connectionManager, migrationFileReader);
            MigrationTool migrationTool = new MigrationTool(migrationManager);
            migrationTool.runMigrations();
        } catch (Exception e) {
            throw new MigrationException("Error occurred during migration process", e);
        }

    }
}