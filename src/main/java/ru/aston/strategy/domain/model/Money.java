package ru.aston.strategy.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
    }

    public static Money of(String amt, String ccy) {
        return new Money(new BigDecimal(amt), Currency.getInstance(ccy));
    }

    public Money plus(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(amount.add(other.amount), currency);
    }

    public Money times(BigDecimal k) {
        return new Money(amount.multiply(k), currency);
    }

    public Money rounded(int scale) {
        return new Money(amount.setScale(scale, RoundingMode.HALF_UP), currency);
    }
}
