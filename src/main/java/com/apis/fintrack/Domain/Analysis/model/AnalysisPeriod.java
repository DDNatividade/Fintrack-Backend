package com.apis.fintrack.domain.analysis.model;

import java.time.LocalDate;

public final class AnalysisPeriod {

    public AnalysisPeriod(LocalDate startDate, LocalDate endDate) {
    }

    /*Analysis for a range of days from today*/
    public static AnalysisPeriod lastDays(int days){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return new AnalysisPeriod(startDate, endDate);
    };

    /*Analysis for a range of months from today*/
    public static AnalysisPeriod lastMonths(int months){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        return new AnalysisPeriod(startDate, endDate);
    };

    /*Analysis for a range of years from today*/
    public static AnalysisPeriod lastYears(int years){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(years);
        return new AnalysisPeriod(startDate, endDate);
    };
}

