package ru.aston.decorator.core;

import ru.aston.decorator.Storage;

import java.io.IOException;

public abstract class StorageDecorator implements Storage {
    protected final Storage delegate;

    protected StorageDecorator(Storage delegate) {
        if (delegate == null) throw new IllegalArgumentException("delegate must not be null");
        this.delegate = delegate;
    }

    @Override
    public void put(String key, byte[] data) throws IOException {
        delegate.put(key, data);
    }

    @Override
    public byte[] get(String key) throws IOException {
        return delegate.get(key);
    }
}