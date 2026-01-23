package com.apis.fintrack.application.budget.mapper;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.port.input.CreateBudgetUseCase;


public class CreateBudgetCommandMapper {
    private CreateBudgetCommandMapper() {
        // Private constructor to prevent instantiation
    }

    public static Budget toDomain(CreateBudgetUseCase.CreateBudgetCommand command) {
        return new Budget(
            null,
            command.userId(),
            command.limitAmount(),
            command.category()
        );
    }
}

