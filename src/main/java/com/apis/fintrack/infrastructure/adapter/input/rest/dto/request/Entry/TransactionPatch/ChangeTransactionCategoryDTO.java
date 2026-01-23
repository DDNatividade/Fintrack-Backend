package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTransactionCategoryDTO {
    private TransactionCategoryEnum category;
}

