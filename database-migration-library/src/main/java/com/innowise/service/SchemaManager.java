package com.innowise.service;

import com.innowise.connection.ConnectionManager;
import com.innowise.constants.SchemaVersionQueries;
import com.innowise.exception.CurrentVersionException;
import com.innowise.exception.MigrationCheckSumException;
import com.innowise.exception.SchemaVersionException;
import com.innowise.model.Migration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * The SchemaManager class is responsible for managing database schema versions
 * It provides methods for creating a schema version table,
 * retrieving the current schema version, and verifying the migration checksum
 */

@Slf4j
@AllArgsConstructor
public class SchemaManager {

    private ConnectionManager connectionManager;

    /**
     * Creates a schema version table in the database
     */
    public void createSchemaVersionTable() {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SchemaVersionQueries.CREATE_TABLE_SCHEMA_VERSION);
            log.info("Schema version table created successfully");
        } catch (SQLException e) {
            log.error("Failed to create schema version table", e);
            throw new SchemaVersionException("Error occurred while creating schema version table", e);
        }
    }

    /**
     * Retrieves the current schema version from the database
     * @return the current schema version as an integer
     * Returns 0 if the version is not found
     */
    public int getCurrentVersion() {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery (SchemaVersionQueries.SELECT_VERSION_FROM_SCHEMA_VERSION)) {

            int currentVersion = rs.next() ? rs.getInt(1) : 0;
            log.info("Successfully received the current version: {}", currentVersion);
            return currentVersion;
        } catch (SQLException e) {
            log.error("Failed to get the current version from the database", e);
            throw new CurrentVersionException("Unable to receive the current version from schema version table", e);
        }
    }

    /**
     * Verifies the checksum for the specified migration object
     * @param migration the migration object for which the checksum needs to be verified
     * @return true if the checksum matches, otherwise false
     */
    public boolean verifyMigrationChecksum(Migration migration) {
        try (Connection connection =  connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SchemaVersionQueries.SELECT_CHECKSUM_FROM_SCHEMA_VERSION)) {
            preparedStatement.setInt(1, migration.getVersion());
            ResultSet rs = preparedStatement.executeQuery();

            log.info("Executing checksum verification for version: " + migration.getVersion());

            if (rs.next()) {
                int checksum = rs.getInt("checksum");
                return checksum == migration.getChecksum();
            }

            log.info("No checksum found for version: " + migration.getVersion());
            return false;
        } catch (SQLException e) {
            log.error("Error verifying checksum for migration version: " + migration.getVersion());
            throw new MigrationCheckSumException("Error verifying checksum for migration version: " + migration.getVersion(), e);
        }
    }
}
