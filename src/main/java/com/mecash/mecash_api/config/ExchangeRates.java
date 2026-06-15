package com.mecash.mecash_api.config;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ExchangeRates {

    public static final BigDecimal A_TO_B = new BigDecimal("1.3455");

    public static final BigDecimal B_TO_A = BigDecimal.ONE.divide(A_TO_B, 10, RoundingMode.HALF_UP);

    private ExchangeRates() {
    }
}
