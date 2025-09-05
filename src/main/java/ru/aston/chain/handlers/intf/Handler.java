package ru.aston.chain.handlers.intf;

import ru.aston.chain.Result;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;

public interface Handler {
    Result handle(SignupContext in, Clock clock);

    String id();
}
