package com.apis.fintrack.domain.analysis.model.kpi;

import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Getter
public final class AnalysisResult {

    private final AnalysisType type;
    private final AnalysisMetric value;
    private final LocalDate calculatedAt;
    private final Map<String, BigDecimal> breakdown;
    private final BigDecimal referenceValue;

    public AnalysisResult(Builder builder) {
        Objects.requireNonNull(builder, "builder cannot be null");
        validateType(builder.type);
        validateValue(builder.value);
        this.type = builder.type;
        this.value = builder.value;
        // Defensive copy to ensure immutability
        this.breakdown = builder.breakdown != null
                ? Map.copyOf(builder.breakdown)
                : null;
        this.referenceValue = builder.referenceValue;
        this.calculatedAt = LocalDate.now();
    }


    /**
     * Creates a simple result with only type and value.
     * Use for KPIs like MONTHLY_AVERAGE, SAVINGS_RATE.
     */
    public static AnalysisResult of(AnalysisType type, AnalysisMetric value) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        return new Builder(type, value).build();
    }

    /**
     * Creates a result with breakdown by category.
     * Use for KPIs like SPENDING_BY_CATEGORY.
     */
    public static AnalysisResult withBreakdown(AnalysisType type, AnalysisMetric value, Map<String, BigDecimal> breakdown) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(breakdown, "breakdown cannot be null");

        return new Builder(type, value).breakdown(breakdown).build();
    }

    /**
     * Creates a result with reference value for ratio calculations.
     * Use for KPIs like SAVINGS_RATE.
     */
    public static AnalysisResult withReference(AnalysisType type, AnalysisMetric value, BigDecimal referenceValue) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(referenceValue, "referenceValue cannot be null");
        return new Builder(type, value).referenceValue(referenceValue).build();
    }

    public boolean hasBreakdown() {
        return breakdown != null && !breakdown.isEmpty();
    }

    public boolean hasReferenceValue() {
        return referenceValue != null;
    }

    private static void validateType(AnalysisType type) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("type cannot be null");
        }
    }

    private static void validateValue(AnalysisMetric value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("value cannot be null");
        }
    }

    public static class Builder {
        private final AnalysisType type;
        private final AnalysisMetric value;
        private Map<String, BigDecimal> breakdown;
        private BigDecimal referenceValue;

        public Builder(AnalysisType type, AnalysisMetric value) {
            this.type = Objects.requireNonNull(type, "type cannot be null");
            this.value = Objects.requireNonNull(value, "value cannot be null");
        }

        public Builder breakdown(Map<String, BigDecimal> breakdown) {
            Objects.requireNonNull(breakdown, "breakdown cannot be null");
            this.breakdown = breakdown;
            return this;
        }

        public Builder referenceValue(BigDecimal referenceValue) {
            Objects.requireNonNull(referenceValue, "referenceValue cannot be null");
            this.referenceValue = referenceValue;
            return this;
        }

        public AnalysisResult build() {
            return new AnalysisResult(this);
        }
    }
}
