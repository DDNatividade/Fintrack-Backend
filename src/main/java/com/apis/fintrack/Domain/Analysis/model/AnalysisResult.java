package com.apis.fintrack.domain.Analysis.model;

import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public final class AnalysisResult {
        private final AnalysisType type;
        private final BigDecimal value;
        private final LocalDate calculatedAt;

        public AnalysisResult(AnalysisType type, BigDecimal value) {
            this.type = type;
            this.value = value;
            this.calculatedAt = LocalDate.now();
        }

}



