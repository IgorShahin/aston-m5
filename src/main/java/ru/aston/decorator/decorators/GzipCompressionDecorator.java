package ru.aston.decorator.decorators;

import ru.aston.decorator.Storage;
import ru.aston.decorator.core.StorageDecorator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class GzipCompressionDecorator extends StorageDecorator {

    public GzipCompressionDecorator(Storage delegate) {
        super(delegate);
    }

    @Override
    public void put(String key, byte[] data) throws IOException {
        byte[] compressed = compress(data);
        super.put(key, compressed);
    }

    @Override
    public byte[] get(String key) throws IOException {
        byte[] raw = super.get(key);
        return decompress(raw);
    }

    private byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(baos)) {
            gos.write(input);
        }
        return baos.toByteArray();
    }

    private byte[] decompress(byte[] input) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(input));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            gis.transferTo(baos);
            return baos.toByteArray();
        }
    }
}