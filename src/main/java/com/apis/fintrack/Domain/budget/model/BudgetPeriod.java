package com.apis.fintrack.domain.budget.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BudgetPeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public BudgetPeriod() {
        this.startDate = LocalDate.now().withDayOfMonth(1);
        this.endDate = startDate.plusMonths(1).minusDays(1);
    }

    @Override
    public String toString() {
        return "BudgetPeriodEnum{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
