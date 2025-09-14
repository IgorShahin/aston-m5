package ru.aston.chain.handlers.impl;

import ru.aston.chain.Result;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;

public final class PasswordStrengthHandler implements Handler {
    @Override
    public String id() {
        return "password-strength";
    }

    @Override
    public Result handle(SignupContext in, Clock clock) {
        String p = in.request().password();
        boolean hasUpper = p.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = p.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = p.chars().anyMatch(Character::isDigit);
        if (!(hasUpper && hasLower && hasDigit)) return Result.stop(in, "weak password");
        return Result.next(in);
    }
}
