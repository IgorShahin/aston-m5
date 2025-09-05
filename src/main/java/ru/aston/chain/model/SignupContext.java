package ru.aston.chain.model;

import java.util.Objects;

public record SignupContext(SignupRequest request, String normalizedEmail, User user) {
    public SignupContext {
        Objects.requireNonNull(request);
    }

    public static SignupContext of(SignupRequest req) {
        return new SignupContext(req, null, null);
    }

    public SignupContext withEmail(String e) {
        return new SignupContext(request, e, user);
    }

    public SignupContext withUser(User u) {
        return new SignupContext(request, normalizedEmail, u);
    }
}
