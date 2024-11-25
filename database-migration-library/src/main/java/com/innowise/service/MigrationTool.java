package com.innowise.service;

import com.innowise.connection.ConnectionManager;
import com.innowise.constants.DatabaseConfig;
import com.innowise.properties.PropertiesUtils;

public class MigrationTool {

    public static void runMigrations() {
        PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();
        String url = propertiesUtils.getProperty(DatabaseConfig.DB_URL);
        String user = propertiesUtils.getProperty(DatabaseConfig.DB_USERNAME);
        String password = propertiesUtils.getProperty(DatabaseConfig.DB_PASSWORD);

        ConnectionManager connectionManager = new ConnectionManager(url, user, password);
        MigrationFileReader migrationFileReader = new MigrationFileReader();
        SchemaManager schemaManager = new SchemaManager(connectionManager);
        MigrationManager migrationManager = new MigrationManager(connectionManager, schemaManager, migrationFileReader);
        migrationManager.applyMigrations();
    }
}
