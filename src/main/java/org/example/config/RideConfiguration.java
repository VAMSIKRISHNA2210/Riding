package org.example.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Configuration class for ride-related values.
 * Values are loaded from application.properties.
 */
@Component
public class RideConfiguration {

    @Getter
    @Value("${ride.service.tax.multiplier}")
    private BigDecimal serviceTaxMultiplier;

    @Getter
    @Value("${ride.base.fare}")
    private BigDecimal baseFare;

    @Getter
    @Value("${ride.distance.fare.rate}")
    private BigDecimal distanceFareRate;

    @Getter
    @Value("${ride.time.fare.rate}")
    private BigDecimal timeFareRate;

    @Getter
    @Value("${ride.max.distance.radius}")
    private BigDecimal maxDistanceRadius;

    @Value("${ride.distance.calculation.context.precision}")
    private int distanceCalculationPrecision;

    public MathContext getDistanceCalculationContext() {
        return new MathContext(distanceCalculationPrecision);
    }
}
