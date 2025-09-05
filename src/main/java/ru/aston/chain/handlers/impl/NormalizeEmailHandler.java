package ru.aston.chain.handlers.impl;

import ru.aston.chain.Result;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;

public final class NormalizeEmailHandler implements Handler {

    @Override
    public Result handle(SignupContext in, Clock clock) {
        String e = in.request().email().trim().toLowerCase();
        int at = e.indexOf('@');
        if (at > 0 && e.endsWith("@gmail.com")) {
            String local = e.substring(0, at).replace(".", "");
            e = local + "@gmail.com";
        }
        return Result.next(in.withEmail(e));
    }

    @Override
    public String id() {
        return "normalize-email";
    }
}
