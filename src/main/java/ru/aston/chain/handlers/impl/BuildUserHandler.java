package ru.aston.chain.handlers.impl;

import ru.aston.chain.Result;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;
import ru.aston.chain.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;

public final class BuildUserHandler implements Handler {
    @Override
    public String id() {
        return "build-user";
    }

    @Override
    public Result handle(SignupContext in, Clock clock) {
        String hash = sha256(in.normalizedEmail());
        return Result.next(in.withUser(new User(hash)));
    }

    private static String sha256(String s) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
