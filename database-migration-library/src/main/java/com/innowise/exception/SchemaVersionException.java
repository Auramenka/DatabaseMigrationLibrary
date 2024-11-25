package com.innowise.exception;

public class SchemaVersionException extends RuntimeException {

    public SchemaVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
