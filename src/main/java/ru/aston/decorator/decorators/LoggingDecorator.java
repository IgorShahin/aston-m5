package ru.aston.decorator.decorators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.decorator.Storage;
import ru.aston.decorator.core.StorageDecorator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class LoggingDecorator extends StorageDecorator {
    private static final Logger log = LoggerFactory.getLogger(LoggingDecorator.class);

    public LoggingDecorator(Storage delegate) {
        super(delegate);
    }

    @Override
    public void put(String key, byte[] data) throws IOException {
        long t0 = System.nanoTime();
        log.info("PUT key='[{}]', bytes=[{}]", key, (data == null ? 0 : data.length));
        try {
            super.put(key, data);
            long ms = (System.nanoTime() - t0) / 1_000_000;
            log.info("PUT done key=[{}] in {} ms", key, ms);
        } catch (IOException e) {
            log.error("PUT failed key=[{}]: [{}]", key, e.toString());
            throw e;
        }
    }

    @Override
    public byte[] get(String key) throws IOException {
        long t0 = System.nanoTime();
        log.info("GET key='{}'", key);
        try {
            byte[] out = super.get(key);
            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0);
            log.info("GET done key=[{}] -> [{}] bytes in {} ms", key, out.length, ms);
            return out;
        } catch (IOException e) {
            log.error("GET failed key=[{}]: [{}]", key, e.toString());
            throw e;
        }
    }
}