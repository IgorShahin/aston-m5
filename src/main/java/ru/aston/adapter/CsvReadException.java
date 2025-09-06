package ru.aston.adapter;

import java.io.IOException;

public class CsvReadException extends IOException {
    public CsvReadException(String message) {
        super(message);
    }

    public CsvReadException(String message, Throwable cause) {
        super(message, cause);
    }
}