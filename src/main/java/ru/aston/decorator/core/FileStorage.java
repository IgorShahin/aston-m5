package ru.aston.decorator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.decorator.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileStorage implements Storage {
    private static final Logger log = LoggerFactory.getLogger(FileStorage.class);
    private final Path root;

    public FileStorage(Path root) throws IOException {
        this.root = root.toAbsolutePath().normalize();
        Files.createDirectories(this.root);
        log.info("Using storage dir: [{}]", this.root);
    }

    @Override
    public void put(String key, byte[] data) throws IOException {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key must not be blank");
        if (data == null) throw new IllegalArgumentException("data must not be null");

        Path p = root.resolve(sanitize(key)).normalize();
        Files.createDirectories(p.getParent());
        Files.write(p, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.debug("Wrote [{}] bytes to [{}]", data.length, p);
    }

    @Override
    public byte[] get(String key) throws IOException {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key must not be blank");

        Path p = root.resolve(sanitize(key)).normalize();
        if (!Files.exists(p)) {
            throw new KeyNotFoundException("Key not found: " + key);
        }
        byte[] data = Files.readAllBytes(p);
        log.debug("Read [{}] bytes from [{}]", data.length, p);
        return data;
    }

    private String sanitize(String key) {
        return key.replace("..", "_");
    }
}