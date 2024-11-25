package com.innowise.service;

import com.innowise.exception.SqLFileReadException;
import com.innowise.model.Migration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

/**
 * The MigrationFileReader class is responsible for reading migration files from a specified directory,
 * extracting their version numbers and SQL content, and returning a sorted list of Migration objects
 */

@Slf4j
public class MigrationFileReader {

    /**
     * Loads migration files from the specified directory
     * @param migrationsDir the directory containing migration files
     * @return a list of Migration objects sorted by their version numbers
     */
    public List<Migration> loadMigrations(String migrationsDir) {
        File migrationsDirectory = new File(migrationsDir);
        File[] migrationFiles = migrationsDirectory.listFiles();

        if (migrationFiles == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(migrationFiles)
                .map(this::getMigration)
                .sorted(Comparator.comparing(Migration::getVersion))
                .collect(Collectors.toList());
    }

    private Migration getMigration(File migrationFile) {
        int version = extractVersionFromFileName(migrationFile.getName());
        String sql = readSqlFromFile(migrationFile);
        int checksum = calculateCheckSum(sql);
        return new Migration(version, sql, checksum);
    }

    private int extractVersionFromFileName(String fileName) {
        String versionStr = fileName.split("__")[0].substring(1);
        return Integer.parseInt(versionStr);
    }

    private String readSqlFromFile(File file) {
        try {
            return Files.lines(file.toPath()).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("Error reading file: " + file.getName(), e);
            throw new SqLFileReadException("Error reading file: " + file.getName(), e);
        }
    }

    private int calculateCheckSum(String sql) {
        CRC32 crc = new CRC32();
        crc.update(sql.getBytes());
        return (int) crc.getValue();
    }
}
