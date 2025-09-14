package ru.aston.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyDemo {
    private static final Logger log = LoggerFactory.getLogger(ProxyDemo.class);

    public static void main(String[] args) {
        Database adminDb = new DatabaseProxy("ADMIN");
        Database guestDb = new DatabaseProxy("GUEST");

        log.info("--- Guest tries to query ---");
        guestDb.query("SELECT * FROM users");

        log.info("--- Admin runs first query ---");
        adminDb.query("SELECT * FROM orders");

        log.info("--- Admin runs second query ---");
        adminDb.query("DELETE FROM orders WHERE id = 5");

        log.info("--- Admin sends invalid query ---");
        adminDb.query("   ");
    }
}
