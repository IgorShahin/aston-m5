package ru.aston.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuilderDemo {
    private static final Logger log = LoggerFactory.getLogger(BuilderDemo.class);

    public static void main(String[] args) {
        User user = User.builder()
                .id("42")
                .name("Игорь")
                .email("igor@example.com")
                .addRole("ADMIN")
                .addRole("USER")
                .build();

        log.info("Создан пользователь: [ {} ]", user);

        User updated = user.toBuilder()
                .email("new@example.com")
                .build();

        log.info("Обновлённый пользователь: [ {} ]", updated);
    }
}
