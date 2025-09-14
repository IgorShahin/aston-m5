package ru.aston.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.decorator.core.FileStorage;
import ru.aston.decorator.core.KeyNotFoundException;
import ru.aston.decorator.decorators.AesGcmEncryptionDecorator;
import ru.aston.decorator.decorators.GzipCompressionDecorator;
import ru.aston.decorator.decorators.LoggingDecorator;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DecoratorDemo {
    private static final Logger log = LoggerFactory.getLogger(DecoratorDemo.class);

    public static void main(String[] args) throws Exception {
        Path root = resolveStoragePath(args);
        SecretKey key = AesGcmEncryptionDecorator.generateKey(256);

        Storage storage = new LoggingDecorator(new AesGcmEncryptionDecorator(new GzipCompressionDecorator(new FileStorage(root)), key));

        String keyName = "docs/readme.txt";
        byte[] payload = loremIpsum().getBytes();

        storage.put(keyName, payload);
        byte[] restored = storage.get(keyName);

        log.info("Data equal after round-trip: [{}]", java.util.Arrays.equals(payload, restored));
        log.info("Original size=[{}], restored size=[{}]", payload.length, restored.length);

        try {
            storage.get("missing/file.bin");
        } catch (KeyNotFoundException e) {
            log.warn("Expected not found: [{}]", e.getMessage());
        } catch (IOException e) {
            log.error("Unexpected IO error: [{}]", e.toString());
        }
    }

    private static Path resolveStoragePath(String[] args) throws IOException {
        String dir = (args != null && args.length > 0) ? args[0] : "storage";
        Path root = Paths.get(dir).toAbsolutePath().normalize();

        if (Files.exists(root) && !Files.isDirectory(root)) {
            throw new IOException("Path is not a directory: " + root);
        }
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        if (!Files.isWritable(root)) {
            throw new IOException("Directory is not writable: " + root);
        }
        log.info("Using storage dir: {}", root);
        return root;
    }

    private static String loremIpsum() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                + "Decorators allow stacking behaviors without modifying the base component.";
    }
}
