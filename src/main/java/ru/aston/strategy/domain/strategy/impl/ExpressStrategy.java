package ru.aston.strategy.domain.strategy.impl;

import ru.aston.strategy.domain.model.Money;
import ru.aston.strategy.domain.model.QuoteRequest;
import ru.aston.strategy.domain.strategy.intf.DeliveryPricingStrategy;

import java.math.BigDecimal;
import java.time.Clock;

public final class ExpressStrategy implements DeliveryPricingStrategy {
    private final Money base;
    private final BigDecimal perKgBoost;
    private final BigDecimal perKmBoost;
    private final BigDecimal rushMultiplier;

    public ExpressStrategy(Money base, BigDecimal perKgBoost, BigDecimal perKmBoost, BigDecimal rushMultiplier) {
        this.base = base;
        this.perKgBoost = perKgBoost;
        this.perKmBoost = perKmBoost;
        this.rushMultiplier = rushMultiplier;
    }

    @Override
    public String id() {
        return "EXPRESS";
    }

    @Override
    public Money price(QuoteRequest r, Clock clock) {
        var cost = base
                .plus(base.times(perKgBoost.multiply(r.weightKg())))
                .plus(base.times(perKmBoost.multiply(r.distanceKm())));
        cost = new Money(cost.amount().multiply(rushMultiplier), cost.currency());
        return cost.rounded(2);
    }
}