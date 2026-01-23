package com.apis.fintrack.application.budget.usecases;

import com.apis.fintrack.domain.budget.port.input.DeleteBudgetUseCase;
import com.apis.fintrack.domain.budget.port.output.BudgetRepositoryPort;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.user.model.UserId;
import java.util.Objects;

/**
 * Application implementation for deleting budgets.
 * Responsibilities: verify existence where appropriate and delegate to repository.
 */
public class DeleteBudgetUseCaseImpl implements DeleteBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;

    public DeleteBudgetUseCaseImpl(BudgetRepositoryPort budgetRepository) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository);
    }

    @Override
    public void deleteById(Long budgetId) {
        Objects.requireNonNull(budgetId, "budgetId cannot be null");
        budgetRepository.deleteById(new BudgetID(budgetId));
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        budgetRepository.deleteAllByUserId(UserId.of(userId));
    }
}
