package com.apis.fintrack.DTO.TransactionEntity.Entry.Patch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTransactionDateDTO {
    private LocalDate transaction_date;
}
