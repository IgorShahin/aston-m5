package ru.aston.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseProxy implements Database {
    private static final Logger log = LoggerFactory.getLogger(DatabaseProxy.class);

    private RealDatabase realDatabase;
    private final String userRole;

    public DatabaseProxy(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public void query(String sql) {
        if (sql == null || sql.isBlank()) {
            log.warn("Empty SQL query provided, skipping execution");
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            log.error("Access denied for role '{}'", userRole);
            return;
        }

        if (realDatabase == null) {
            log.info("Lazy initialization of RealDatabase for role '{}'", userRole);
            realDatabase = new RealDatabase();
        }

        log.info("Delegating query execution to RealDatabase");
        realDatabase.query(sql);
    }
}
