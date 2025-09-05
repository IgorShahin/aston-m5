package ru.aston.chain.model;

import java.util.Objects;

public record SignupRequest(String email, String password) {
    public SignupRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
