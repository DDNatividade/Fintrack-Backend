package com.apis.fintrack.domain.analysis.model;

import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Getter
public final class AnalysisResult {

    private final AnalysisType type;
    private final AnalysisMetric value;                     // valor principal
    private final LocalDate calculatedAt;
    private final Map<String, BigDecimal> breakdown;    // para KPIs por categoría
    private final BigDecimal referenceValue;            // para ratios (por ejemplo, total ingresos en SavingsRate)

    public AnalysisResult(AnalysisType type, AnalysisMetric value) {
        this(type, value, null, null);
    }

    public AnalysisResult(AnalysisType type, AnalysisMetric value, Map<String, BigDecimal> breakdown, BigDecimal referenceValue) {
        validateType(type);
        validateValue(value);
        this.type = type;
        this.value = value;
        this.breakdown = breakdown;
        this.referenceValue = referenceValue;
        this.calculatedAt = LocalDate.now();
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
}
