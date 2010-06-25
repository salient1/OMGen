package com.omgen.generator.exception;

/**
 *
 */
public class NoSetterMethodsException extends RuntimeException {
    public NoSetterMethodsException() {
    }

    public NoSetterMethodsException(String message) {
        super(message);
    }

    public NoSetterMethodsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSetterMethodsException(Throwable cause) {
        super(cause);
    }
}
