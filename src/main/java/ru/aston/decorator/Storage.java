package ru.aston.decorator;

import java.io.IOException;

public interface Storage {
    void put(String key, byte[] data) throws IOException;

    byte[] get(String key) throws IOException;
}
