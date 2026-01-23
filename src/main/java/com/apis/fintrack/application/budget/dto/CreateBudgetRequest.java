package com.apis.fintrack.application.budget.dto;

import java.math.BigDecimal;

/**
 * Simple immutable DTO used by the application layer to carry primitive input
 * for budget creation. This DTO is intentionally primitive: it should be
 * populated by the REST adapter and then converted to domain value objects
 * by the mapper.
 *
 * @param currency optional, ISO-4217 code; may be null
 * @param category optional, e.g. "FOOD"; may be null
 */
public record CreateBudgetRequest(Long userId, BigDecimal limitAmount, String currency, String category) {

}

