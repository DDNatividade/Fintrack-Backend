package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDTO {
    private TransactionCategoryEnum category;
    private String description;
    private BigDecimal amount;
    private boolean isIncome;

}

