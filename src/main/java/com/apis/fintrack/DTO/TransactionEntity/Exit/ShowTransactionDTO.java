package com.apis.fintrack.DTO.TransactionEntity.Exit;

import com.apis.fintrack.Entity.CategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ShowTransaction(
        String description,
        LocalDate transaction_date,
        BigDecimal amount,
        CategoryEnum category

) {
}
