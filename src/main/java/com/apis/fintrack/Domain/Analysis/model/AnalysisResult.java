package com.apis.fintrack.domain.analysis.model;

import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public final class AnalysisResult {
        private final AnalysisType type;
        private final BigDecimal value;
        private final LocalDate calculatedAt;

        public AnalysisResult(AnalysisType type, BigDecimal value) {
            validateType(type);
            validateValue(value);

            this.type = type;
            this.value = value;
            this.calculatedAt = LocalDate.now();
        }

        // Valida que el tipo no sea null ni 'empty' (para enum solo null aplica)
        private static void validateType(AnalysisType type) {
            if (Objects.isNull(type)) {
                throw new IllegalArgumentException("El parámetro 'type' no puede ser null o vacío");
            }
        }

        // Valida que el valor no sea null.
        // NOTA: permitimos valor 0 ya que es un resultado válido de algunos análisis.
        private static void validateValue(BigDecimal value) {
            if (Objects.isNull(value)) {
                throw new IllegalArgumentException("El parámetro 'value' no puede ser null");
            }
        }

}
