package com.innowise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.innowise.exception.JsonReportGenerationException;
import com.innowise.model.MigrationResultRecords;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Migration class is responsible for collecting migration results
 * and generating a JSON report of these results
 */

@Slf4j
public class MigrationReport {

    private final List<MigrationResultRecords> results = new ArrayList<>();

    /**
     * Adds a migration result to the report
     * @param version the version number of the migration
     * @param isSuccess indicates whether the migration was successful
     * @param message a message providing additional details about the migration result
     */
    public void addMigrationResult(int version, boolean isSuccess, String message) {
        results.add(new MigrationResultRecords(version, isSuccess, message));
    }

    /**
     * Generates a JSON report of the migration results and writes it to a specified file
     * @param filePath the path of the file where the JSON report will be written
     * @throws JsonReportGenerationException if there is an error generating the JSON report
     */
    public void generateJsonReport(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File(filePath), results);
            log.info("JSON report successfully generated at: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to generate JSON report: {}", e.getMessage());
            throw new JsonReportGenerationException("Error generating JSON report", e);
        }
    }
}
