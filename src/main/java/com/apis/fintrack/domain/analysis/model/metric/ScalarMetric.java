package com.apis.fintrack.domain.analysis.model.metric;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Scalar metric representing a single numeric value.
 * Typical use cases: KPIs, percentages, averages.
 */
public record ScalarMetric(BigDecimal value, MetricUnit unit)
        implements AnalysisMetric {

    public ScalarMetric {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(unit, "unit must not be null");
    }
}

