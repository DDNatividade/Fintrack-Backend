package com.apis.fintrack.domain.analysis.model.kpi;

import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import lombok.Getter;

@Getter
public enum AnalysisType {
        SAVINGS_RATE(MetricUnit.PERCENT),
        MONTHLY_AVERAGE_EXPENSES(MetricUnit.CURRENCY),
        SPENDING_TREND(MetricUnit.PERCENT);

        private final MetricUnit unit;

        AnalysisType(MetricUnit unit) {
            this.unit = unit;
        }

        public MetricUnit unit() {
            return unit;
        }


    /**
     * Parsea un string a AnalysisType de forma segura (ignora mayúsculas/minúsculas).
     * Lanza IllegalArgumentException si el input es null/empty o no corresponde a ningún tipo.
     */
    public static AnalysisType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro 'type' no puede ser null o vacío");
        }
        try {
            return AnalysisType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de análisis no válido: '" + value + "'");
        }
    }
}
