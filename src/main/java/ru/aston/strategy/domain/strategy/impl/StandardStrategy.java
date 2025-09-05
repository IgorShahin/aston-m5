package ru.aston.strategy.domain.strategy.impl;

import ru.aston.strategy.domain.model.Money;
import ru.aston.strategy.domain.model.QuoteRequest;
import ru.aston.strategy.domain.strategy.intf.DeliveryPricingStrategy;

import java.math.BigDecimal;
import java.time.Clock;

public final class StandardStrategy implements DeliveryPricingStrategy {
    private final Money base;
    private final BigDecimal perKg;
    private final BigDecimal perKm;
    private final Money fragileFee;
    private final BigDecimal insurancePct;

    public StandardStrategy(Money base, BigDecimal perKg, BigDecimal perKm,
                            Money fragileFee, BigDecimal insurancePct) {
        this.base = base;
        this.perKg = perKg;
        this.perKm = perKm;
        this.fragileFee = fragileFee;
        this.insurancePct = insurancePct;
    }

    @Override
    public String id() {
        return "STANDARD";
    }

    @Override
    public Money price(QuoteRequest r, Clock clock) {
        var cost = base
                .plus(base.times(perKg.multiply(r.weightKg())))
                .plus(base.times(perKm.multiply(r.distanceKm())));
        if (r.fragile()) cost = cost.plus(fragileFee);
        if (r.insured()) cost = cost.plus(cost.times(insurancePct));
        return cost.rounded(2);
    }
}