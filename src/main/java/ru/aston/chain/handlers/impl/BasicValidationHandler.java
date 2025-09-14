package ru.aston.chain.handlers.impl;

import ru.aston.chain.Result;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;
import java.util.regex.Pattern;

public final class BasicValidationHandler implements Handler {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public String id() {
        return "basic-validation";
    }

    @Override
    public Result handle(SignupContext in, Clock clock) {
        String email = in.normalizedEmail();
        String pwd = in.request().password();
        if (email == null || !EMAIL.matcher(email).matches()) return Result.stop(in, "invalid email");
        if (pwd.length() < 8) return Result.stop(in, "password too short");
        return Result.next(in);
    }
}