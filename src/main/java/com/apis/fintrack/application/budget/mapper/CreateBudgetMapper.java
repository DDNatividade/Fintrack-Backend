package com.apis.fintrack.application.budget.mapper;

import com.apis.fintrack.application.budget.dto.CreateBudgetRequest;
import com.apis.fintrack.domain.budget.port.input.CreateBudgetUseCase;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import java.util.Currency;
import java.util.Objects;

/**
 * Mapper responsible for converting primitive application DTOs into the
 * CreateBudgetCommand expected by the domain input port.
 * Important: This mapper does NOT create aggregates. It only constructs
 * domain value objects (UserId, Money, enums) and the command record.
 */
public final class CreateBudgetMapper {

    private CreateBudgetMapper() {}

    public static CreateBudgetUseCase.CreateBudgetCommand toCommand(CreateBudgetRequest request) {
        Objects.requireNonNull(request, "request cannot be null");
        if (request.userId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (request.limitAmount() == null) {
            throw new IllegalArgumentException("limitAmount is required");
        }

        UserId userId = UserId.of(request.userId());

        Currency currency = parseCurrencyOrDefault(request.currency());
        Money limit = Money.of(request.limitAmount(), currency);

        TransactionCategoryEnum category = parseCategoryOrDefault(request.category());

        return new CreateBudgetUseCase.CreateBudgetCommand(userId, limit, category);
    }

    private static Currency parseCurrencyOrDefault(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) {
            return Currency.getInstance("EUR");
        }
        try {
            return Currency.getInstance(currencyCode.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currencyCode);
        }
    }

    private static TransactionCategoryEnum parseCategoryOrDefault(String category) {
        if (category == null || category.isBlank()) {
            return TransactionCategoryEnum.OTHER;
        }
        try {
            return TransactionCategoryEnum.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
}

