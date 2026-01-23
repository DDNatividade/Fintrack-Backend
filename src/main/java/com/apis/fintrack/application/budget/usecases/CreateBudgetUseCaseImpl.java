package com.apis.fintrack.application.budget.usecases;

import com.apis.fintrack.application.budget.mapper.CreateBudgetCommandMapper;
import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.port.input.CreateBudgetUseCase;
import com.apis.fintrack.domain.budget.port.output.BudgetRepositoryPort;
import java.util.Objects;

/**
 * Application layer implementation of CreateBudgetUseCase.
 * Responsibilities:
 * - Check application-level constraints (duplicate budgets per user+category)
 * - Create the Budget aggregate and persist via BudgetRepositoryPort
 * - Keep domain invariants inside the Budget constructor
 */
public class CreateBudgetUseCaseImpl implements CreateBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;

    public CreateBudgetUseCaseImpl(BudgetRepositoryPort budgetRepository) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository, "budgetRepository required");
    }

    @Override
    public Budget create(CreateBudgetCommand command) {
        if(budgetRepository.existsByUserIdAndCategory(
                command.userId(),
                command.category())) {
            throw new IllegalArgumentException("Budget already exists for user and category");
        }
        Objects.requireNonNull(command, "command cannot be null");
        return CreateBudgetCommandMapper.toDomain(command);
    }
}
