package ru.aston.strategy;

import ru.aston.strategy.application.registry.StrategyRegistry;
import ru.aston.strategy.application.service.PricingService;
import ru.aston.strategy.domain.model.Money;
import ru.aston.strategy.domain.model.QuoteRequest;
import ru.aston.strategy.domain.model.ServiceLevel;
import ru.aston.strategy.domain.strategy.impl.ExpressStrategy;
import ru.aston.strategy.domain.strategy.impl.StandardStrategy;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Map;

/**
 * Демо применения паттерна «Стратегия».
 * <p>
 * Здесь мы имитируем сервис расчёта стоимости доставки.
 * У нас есть две стратегии:
 * <p>
 * StandardStrategy - обычная доставка (цена зависит от веса, расстояния,
 * с надбавкой за хрупкость и страховку).
 * <p>
 * ExpressStrategy - экспресс-доставка (усиленные коэффициенты и множитель
 * за срочность).
 * <p>
 * Стратегии регистрируются в реестре (StrategyRegistry), откуда
 * PricingService выбирает нужную по ключу (STANDARD или EXPRESS).
 */
public class StrategyDemo {
    public static void main(String[] args) {
        var standard = new StandardStrategy(
                Money.of("5.00", "EUR"),
                new BigDecimal("0.10"),
                new BigDecimal("0.02"),
                Money.of("3.00", "EUR"),
                new BigDecimal("0.01")
        );

        var express = new ExpressStrategy(
                Money.of("8.00", "EUR"),
                new BigDecimal("0.18"),
                new BigDecimal("0.03"),
                new BigDecimal("1.35")
        );

        var registry = new StrategyRegistry(Map.of(
                standard.id(), standard,
                express.id(), express
        ));

        var service = new PricingService(registry, Clock.systemUTC());

        var reqStd = new QuoteRequest(new BigDecimal("12.5"), new BigDecimal("180"), ServiceLevel.STANDARD, true, true);
        service.quote(reqStd);
        var reqExp = new QuoteRequest(new BigDecimal("7.2"), new BigDecimal("60"), ServiceLevel.EXPRESS, false, false);
        service.quote(reqExp);
    }
}
