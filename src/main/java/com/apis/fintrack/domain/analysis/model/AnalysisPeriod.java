package com.apis.fintrack.domain.analysis.model;

import java.time.LocalDate;
/**
 * Represents a period for financial analysis with a start and end date.
 *
 * @param startDate the start date of the analysis period
 * @param endDate   the end date of the analysis period
 */
public record AnalysisPeriod(LocalDate startDate, LocalDate endDate) {

    public AnalysisPeriod {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "startDate (" + startDate + ") must not be after endDate (" + endDate + ")"
            );
        }
    }

    public static AnalysisPeriod lastDays(int days) {
        validateNonNegative(days, "days");
        if (days > 360) {
            throw new IllegalArgumentException(
                    "Days must not exceed 360 (1 year). Provided: " + days
            );
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return new AnalysisPeriod(startDate, endDate);
    }

    public static AnalysisPeriod lastMonths(int months) {
        validateNonNegative(months, "months");
        if (months > 120) {
            throw new IllegalArgumentException(
                    "Months must not exceed 120 (10 years). Provided: " + months
            );
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        return new AnalysisPeriod(startDate, endDate);
    }

    public static AnalysisPeriod lastYears(int years) {
        validateNonNegative(years, "years");
        if (years > 10) {
            throw new IllegalArgumentException(
                    "Years must not exceed 10. Provided: " + years
            );
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(years);
        return new AnalysisPeriod(startDate, endDate);
    }

    private static void validateNonNegative(int value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    paramName + " must be non-negative. Provided: " + value
            );
        }
    }
}
