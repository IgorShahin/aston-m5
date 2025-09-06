package ru.aston.decorator.core;

import java.io.IOException;

public class KeyNotFoundException extends IOException {
    public KeyNotFoundException(String message) {
        super(message);
    }

    public KeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}