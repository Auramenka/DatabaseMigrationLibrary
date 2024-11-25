package com.innowise.service;

import com.innowise.connection.ConnectionManager;
import com.innowise.constants.SchemaVersionQueries;
import com.innowise.exception.ConnectionException;
import com.innowise.exception.RollbackException;
import com.innowise.exception.SchemaVersionException;
import com.innowise.model.Migration;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * The MigrationExecutor class is responsible for executing database migrations
 * It handles the migration process, including connecting to the database,
 * executing SQL statements, and managing transaction commits and rollbacks
 */

@Slf4j
public class MigrationExecutor {

    /**
     * Executes a database migration
     * @param migration the Migration object containing the migration details (version, SQL script, checksum)
     * @param connectionManager the ConnectionManager used to obtain a database connection
     * @throws RollbackException if the transaction cannot be rolled back after a failed migration
     * @throws ConnectionException if there is an error closing the connection
     */
    public static void executeMigration(Migration migration, ConnectionManager connectionManager) {

        Connection connection = null;

        log.info("Starting migration with version: " + migration.getVersion());

        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);

            statement.executeQuery(SchemaVersionQueries.SELECT_ALL_FROM_SCHEMA_VERSION_FOR_UPDATE);
            statement.executeUpdate(migration.getSql());

            insertSchemaVersion(migration.getVersion(), migration.getChecksum(), connection);

            connection.commit();
            log.info("Migration with version " + migration.getVersion() + " completed successfully");

        } catch (SQLException e) {
            try {
                connection.rollback();
                log.info("Rollback completed for migration with version: " + migration.getVersion(), e);
            } catch (SQLException ex) {
                log.error("Failed to rollback migration with version " + migration.getVersion(), ex);
                throw new RollbackException("Failed to rollback migration with version " + migration.getVersion(), ex);
            }
        } finally {
            try {
                connection.close();
                log.info("Connection closed for migration with version: " + migration.getVersion());
            } catch (SQLException e) {
                log.error("Failed to close connection for migration with version " + migration.getVersion(), e);
                throw new ConnectionException("Failed to close connection for migration with version" + migration.getVersion(), e);
            }
        }
    }

    private static void insertSchemaVersion(int version, int checksum, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SchemaVersionQueries.INSERT_INTO_SCHEMA_VERSION)) {
            preparedStatement.setInt(1, version);
            preparedStatement.setInt(2, checksum);

            preparedStatement.executeUpdate();
            log.info("Schema version inserted successfully: version={}, checksum={}", version, checksum);
        } catch (SQLException e) {
            log.error("Failed to insert schema version: version={}, checksum={}", version, checksum, e);
            throw new SchemaVersionException("Error inserting schema version: " + version, e);
        }
    }
}
