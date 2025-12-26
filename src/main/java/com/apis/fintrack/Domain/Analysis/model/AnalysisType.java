package com.apis.fintrack.domain.analysis.model;

import lombok.Getter;

@Getter
public enum AnalysisType {
    MONTHLY_AVERAGE,
    SAVINGS_RATE,
    SPENDING_TREND;

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
