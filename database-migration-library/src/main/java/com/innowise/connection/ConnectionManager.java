package com.innowise.connection;

import com.innowise.exception.ConnectionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The ConnectionManager class is responsible for managing database connections
 * It provides a method to create and return a database connection
 */

@Slf4j
@AllArgsConstructor
public class ConnectionManager {

    private String url;
    private String user;
    private String password;

    /**
     * Establishes a connection to the database using the provided URL, user, and password
     *
     * @return a Connection object representing the database connection
     * @throws ConnectionException If the connection to the database could not be established
     */
    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            log.info("Successfully connected to the database at {}", url);
            return connection;
        } catch (SQLException e) {
            log.error("Failed to connect to the database at {}: {}", url, e.getMessage());
            throw new ConnectionException("Failed to create connection", e);
        }
    }
}
