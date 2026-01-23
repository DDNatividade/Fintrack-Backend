package com.apis.fintrack.application.analysis.readmodel;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

/**
 * Read model representing monthly cashflow.
 */
public record MonthlyCashflowRM(
        Map<YearMonth, BigDecimal> income,
        Map<YearMonth, BigDecimal> expenses
) {
}
