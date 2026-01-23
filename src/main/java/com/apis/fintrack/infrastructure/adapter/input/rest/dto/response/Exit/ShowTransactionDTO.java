package com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ShowTransactionDTO(
        String description,
        LocalDate transaction_date,
        BigDecimal amount,
        TransactionCategoryEnum category
) {
}

