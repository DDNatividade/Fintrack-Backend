package com.apis.fintrack.domain.analysis.model.metric;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Distribution metric representing a breakdown of values by category.
 */
public record DistributionMetric(Map<TransactionCategoryEnum, BigDecimal> distribution) implements AnalysisMetric {

    public DistributionMetric(Map<TransactionCategoryEnum, BigDecimal> distribution) {
        Objects.requireNonNull(distribution, "distribution must not be null");
        this.distribution = Collections.unmodifiableMap(distribution);
    }

}
