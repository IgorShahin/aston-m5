package ru.aston.chain;

import ru.aston.chain.model.SignupContext;

public record Result(Outcome outcome, SignupContext ctx, String reason) {
    public enum Outcome {CONTINUE, STOP}

    public static Result next(SignupContext ctx) {
        return new Result(Outcome.CONTINUE, ctx, null);
    }

    public static Result stop(SignupContext ctx, String reason) {
        return new Result(Outcome.STOP, ctx, reason);
    }
}
