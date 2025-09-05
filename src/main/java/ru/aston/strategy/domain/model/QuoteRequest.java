package ru.aston.strategy.domain.model;


import java.math.BigDecimal;
import java.util.Objects;

public record QuoteRequest(BigDecimal weightKg, BigDecimal distanceKm, ServiceLevel level,
                           boolean fragile, boolean insured) {
    public QuoteRequest {
        Objects.requireNonNull(weightKg);
        Objects.requireNonNull(distanceKm);
        Objects.requireNonNull(level);
        if (weightKg.signum() <= 0) throw new IllegalArgumentException("weight must be > 0");
        if (distanceKm.signum() < 0) throw new IllegalArgumentException("distance must be >= 0");
    }
}