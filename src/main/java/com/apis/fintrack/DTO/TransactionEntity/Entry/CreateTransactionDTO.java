package com.apis.fintrack.DTO.TransactionEntity.Entry;

import com.apis.fintrack.Entity.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDTO {
    private CategoryEnum category;
    private String description;
    private BigDecimal amount;
    private boolean isIncome;

}
