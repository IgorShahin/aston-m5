package ru.aston.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealDatabase implements Database {
    private static final Logger log = LoggerFactory.getLogger(RealDatabase.class);

    public RealDatabase() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Real database connection established");
    }

    @Override
    public void query(String sql) {
        log.debug("Executing SQL: [{}]", sql);
    }
}
