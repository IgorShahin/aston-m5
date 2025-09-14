package ru.aston.strategy.application.registry;

import ru.aston.strategy.domain.strategy.intf.DeliveryPricingStrategy;

import java.util.Map;

public final class StrategyRegistry {
    private final Map<String, DeliveryPricingStrategy> map;

    public StrategyRegistry(Map<String, DeliveryPricingStrategy> map) {
        this.map = Map.copyOf(map);
    }

    public DeliveryPricingStrategy getOrDefault(String id, String fallbackId) {
        return map.getOrDefault(id, map.get(fallbackId));
    }
}
