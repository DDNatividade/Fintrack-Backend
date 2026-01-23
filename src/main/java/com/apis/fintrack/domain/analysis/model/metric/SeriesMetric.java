package com.apis.fintrack.domain.analysis.model.metric;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Time series metric where values evolve over time.
 */
public record SeriesMetric(Map<YearMonth, BigDecimal> series) implements AnalysisMetric {

    public SeriesMetric(Map<YearMonth, BigDecimal> series) {
        Objects.requireNonNull(series, "series must not be null");
        this.series = Collections.unmodifiableMap(series);
    }

}
