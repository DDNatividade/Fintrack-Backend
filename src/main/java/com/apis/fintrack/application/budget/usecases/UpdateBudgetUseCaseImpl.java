package com.apis.fintrack.application.budget.usecases;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.budget.port.input.UpdateBudgetUseCase;
import com.apis.fintrack.domain.budget.port.output.BudgetRepositoryPort;
import com.apis.fintrack.domain.budget.exception.BudgetNotFoundException;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.shared.model.Money;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Application implementation for UpdateBudgetUseCase.
 * Responsibilities:
 * - load aggregate, apply operations and persist
 * - enforce application level rules (e.g., category duplicates) via repository checks
 */
public class UpdateBudgetUseCaseImpl implements UpdateBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;

    public UpdateBudgetUseCaseImpl(BudgetRepositoryPort budgetRepository) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository);
    }

    private Budget loadOrThrow(Long budgetId) {
        return budgetRepository.findById(new BudgetID(budgetId))
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));
    }

    @Override
    public Budget updateLimit(Long budgetId, BigDecimal newLimit) {
        Objects.requireNonNull(newLimit, "newLimit cannot be null");
        Budget b = loadOrThrow(budgetId);
        Money money = Money.of(newLimit);
        b.changeLimit(money);
        return budgetRepository.save(b);
    }

    @Override
    public Budget updateCategory(Long budgetId, TransactionCategoryEnum newCategory) {
        Objects.requireNonNull(newCategory, "newCategory cannot be null");
        Budget b = loadOrThrow(budgetId);
        // application-level check: ensure no other budget exists for the new category
        if (budgetRepository.existsByUserIdAndCategory(b.getUserId(), newCategory)) {
            throw new com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists("Budget with category already exists");
        }
        b.changeCategory(newCategory);
        return budgetRepository.save(b);
    }

    @Override
    public Budget update(UpdateBudgetCommand command) {
        Objects.requireNonNull(command, "command cannot be null");
        Long budgetId = command.budgetId();
        Budget b = loadOrThrow(budgetId);
        if (command.newLimitAmount() != null) {
            Money money = Money.of(command.newLimitAmount());
            b.changeLimit(money);
        }
        if (command.newCategory() != null && !command.newCategory().isBlank()) {
            TransactionCategoryEnum parsed;
            try {
                parsed = TransactionCategoryEnum.valueOf(command.newCategory().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + command.newCategory());
            }
            if (budgetRepository.existsByUserIdAndCategory(b.getUserId(), parsed)) {
                throw new com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists("Budget with category already exists");
            }
            b.changeCategory(parsed);
        }
        return budgetRepository.save(b);
    }
}

