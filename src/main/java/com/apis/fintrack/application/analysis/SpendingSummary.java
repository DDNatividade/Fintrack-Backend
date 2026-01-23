package com.apis.fintrack.application.analysis;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * DTO de resumen de gastos para ser mostrado al cliente.
 * Es inmutable y contiene la información agregada calculada por los casos de uso.
 */
public record SpendingSummary(BigDecimal totalSpending, LocalDate periodStart, LocalDate periodEnd,
                              Map<String, BigDecimal> breakdownByCategory, int countTransactions) {

    public SpendingSummary(BigDecimal totalSpending,
                           LocalDate periodStart,
                           LocalDate periodEnd,
                           Map<String, BigDecimal> breakdownByCategory,
                           int countTransactions) {
        this.totalSpending = Objects.requireNonNull(totalSpending, "totalSpending must not be null");
        this.periodStart = Objects.requireNonNull(periodStart, "periodStart must not be null");
        this.periodEnd = Objects.requireNonNull(periodEnd, "periodEnd must not be null");
        if (periodStart.isAfter(periodEnd)) {
            throw new IllegalArgumentException("periodStart must be on or before periodEnd");
        }
        // defensively copy the map
        if (breakdownByCategory == null) {
            this.breakdownByCategory = Collections.emptyMap();
        } else {
            Map<String, BigDecimal> copy = new HashMap<>(breakdownByCategory.size());
            for (Map.Entry<String, BigDecimal> e : breakdownByCategory.entrySet()) {
                String k = Objects.requireNonNull(e.getKey(), "breakdown category key must not be null");
                BigDecimal v = e.getValue() == null ? BigDecimal.ZERO : e.getValue();
                copy.put(k, v);
            }
            this.breakdownByCategory = Collections.unmodifiableMap(copy);
        }
        if (countTransactions < 0) {
            throw new IllegalArgumentException("countTransactions must not be negative");
        }
        this.countTransactions = countTransactions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpendingSummary that = (SpendingSummary) o;
        return countTransactions == that.countTransactions &&
                totalSpending.equals(that.totalSpending) &&
                periodStart.equals(that.periodStart) &&
                periodEnd.equals(that.periodEnd) &&
                breakdownByCategory.equals(that.breakdownByCategory);
    }

    @Override
    public String toString() {
        return "SpendingSummary{" +
                "totalSpending=" + totalSpending +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", breakdownByCategory=" + breakdownByCategory +
                ", countTransactions=" + countTransactions +
                '}';
    }

    /**
     * Factory helper para crear un resumen vacío para un periodo dado.
     */
    public static SpendingSummary empty(LocalDate start, LocalDate end) {
        return new SpendingSummary(BigDecimal.ZERO, start, end, Collections.emptyMap(), 0);
    }
}

