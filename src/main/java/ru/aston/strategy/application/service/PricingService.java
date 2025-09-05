package ru.aston.strategy.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.strategy.application.registry.StrategyRegistry;
import ru.aston.strategy.domain.model.QuoteRequest;

import java.time.Clock;

public final class PricingService {
    private static final Logger log = LoggerFactory.getLogger(PricingService.class);

    private final StrategyRegistry registry;
    private final Clock clock;

    public PricingService(StrategyRegistry registry, Clock clock) {
        this.registry = registry;
        this.clock = clock;
    }

    public void quote(QuoteRequest r) {
        var strat = registry.getOrDefault(r.level().name(), "STANDARD");
        var start = System.nanoTime();
        var price = strat.price(r, clock);
        log.debug("strategy=[{}] result=[{}] [{}] in [{}] ms",
                strat.id(), price.amount(), price.currency(),
                (System.nanoTime() - start) / 1_000_000.0);
    }
}