package ru.aston.chain.handlers.impl;

import ru.aston.chain.Result;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;
import java.util.Set;

public final class EmailNotTakenHandler implements Handler {
    private final Set<String> alreadyTaken;

    public EmailNotTakenHandler(Set<String> alreadyTaken) {
        this.alreadyTaken = Set.copyOf(alreadyTaken);
    }

    @Override
    public String id() {
        return "email-unique";
    }

    @Override
    public Result handle(SignupContext in, Clock clock) {
        if (alreadyTaken.contains(in.normalizedEmail())) return Result.stop(in, "email already used");
        return Result.next(in);
    }
}