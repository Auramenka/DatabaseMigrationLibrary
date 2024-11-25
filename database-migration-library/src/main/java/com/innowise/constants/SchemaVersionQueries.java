package com.innowise.constants;

public class SchemaVersionQueries {

    public static final String CREATE_TABLE_SCHEMA_VERSION = "CREATE TABLE IF NOT EXISTS schema_version(version INT PRIMARY KEY, checksum INT)";
    public static final String SELECT_VERSION_FROM_SCHEMA_VERSION = "SELECT version FROM schema_version ORDER BY version DESC LIMIT 1";
    public static final String INSERT_INTO_SCHEMA_VERSION = "INSERT INTO schema_version(version, checksum) VALUES (?, ?)";
    public static final String SELECT_ALL_FROM_SCHEMA_VERSION_FOR_UPDATE = "SELECT * FROM schema_version FOR UPDATE";
    public static final String SELECT_CHECKSUM_FROM_SCHEMA_VERSION = "SELECT checksum FROM schema_version WHERE version = ?";
}