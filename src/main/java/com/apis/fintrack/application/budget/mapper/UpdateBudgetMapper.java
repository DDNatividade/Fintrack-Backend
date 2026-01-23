package com.apis.fintrack.application.budget.mapper;

import com.apis.fintrack.application.budget.dto.UpdateBudgetRequest;
import com.apis.fintrack.domain.budget.port.input.UpdateBudgetUseCase;

import java.util.Objects;

/**
 * Mapper for update budget operations. Converts primitive request data into
 * the domain command (UpdateBudgetCommand) used by the input port.
 * This mapper does not apply business rules or fetch aggregates.
 */
public final class UpdateBudgetMapper {

    private UpdateBudgetMapper() {}

    public static UpdateBudgetUseCase.UpdateBudgetCommand toCommand(UpdateBudgetRequest request) {
        Objects.requireNonNull(request, "request cannot be null");
        if (request.budgetId() == null) {
            throw new IllegalArgumentException("budgetId is required");
        }

        return new UpdateBudgetUseCase.UpdateBudgetCommand(
                request.budgetId(),
                request.newLimitAmount(),
                request.newCategory()
        );
    }
}
