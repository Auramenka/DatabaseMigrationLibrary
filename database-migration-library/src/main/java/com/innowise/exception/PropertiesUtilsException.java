package com.innowise.exception;

public class PropertiesUtilsException extends RuntimeException {

    public PropertiesUtilsException(String message) {
        super(message);
    }

    public PropertiesUtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
