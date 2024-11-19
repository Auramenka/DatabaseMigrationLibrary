package com.innowise.service;

import com.innowise.connection.ConnectionManager;
import com.innowise.constants.ResourcesConstants;
import com.innowise.model.Migration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The MigrationManager class is responsible for applying database migrations
 * This class interacts with {@link ConnectionManager} to manage database connections,
 * {@link SchemaManager} to manage the database schema,
 * {@link MigrationFileReader} to load migration files
 */

@Slf4j
@AllArgsConstructor
public class MigrationManager {

    private ConnectionManager connectionManager;
    private SchemaManager schemaManager;
    private MigrationFileReader migrationFileReader;

    /**
     * Applies all available migrations
     * This method creates the schema version table (if it has not been created yet)
     * and loads migrations from files located at the specified resource
     * It also generates a migration application report in JSON format
     */
    public void applyMigrations() {
        schemaManager.createSchemaVersionTable();
        List<Migration> migrations = migrationFileReader.loadMigrations(getClass().getClassLoader()
                .getResource(ResourcesConstants.RESOURCE_MIGRATION_FOLDER).getPath());

        int currentVersion = schemaManager.getCurrentVersion();
        MigrationReport report = new MigrationReport();

        for (Migration migration : migrations) {
            processMigration(currentVersion, report, migration);
        }
        report.generateJsonReport("migration-report.json");
    }

    private void processMigration(int currentVersion, MigrationReport report, Migration migration) {
        try {
            if (isMigrationAlreadyApplied(report, migration)) return;

            if (migration.getVersion() > currentVersion) {
                executeMigration(report, migration);
            }
        } catch (Exception e) {
            reportMigrationFailure(report, migration, e);
        }
    }

    private void reportMigrationFailure(MigrationReport report, Migration migration, Exception e) {
        report.addMigrationResult(migration.getVersion(), false, e.getMessage());
        log.error("Migration failed for version {}: {}", migration.getVersion(), e.getMessage());
    }

    private void executeMigration(MigrationReport report, Migration migration) {
        MigrationExecutor.executeMigration(migration, connectionManager);
        report.addMigrationResult(migration.getVersion(), true, "Migration executed successfully");
    }

    private boolean isMigrationAlreadyApplied(MigrationReport report, Migration migration) {
        if (schemaManager.verifyMigrationChecksum(migration)) {
            log.info("Migration already applied: {}", migration.getVersion());
            report.addMigrationResult(migration.getVersion(), true, "Migration already applied");
            return true;
        }
        return false;
    }
}
