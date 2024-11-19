package com.innowise.exception;

public class CurrentVersionException extends RuntimeException {

    public CurrentVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
