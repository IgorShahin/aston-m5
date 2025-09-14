package ru.aston.strategy.domain.strategy.intf;

import ru.aston.strategy.domain.model.Money;
import ru.aston.strategy.domain.model.QuoteRequest;

import java.time.Clock;

public interface DeliveryPricingStrategy {
    String id();

    Money price(QuoteRequest r, Clock clock);
}
