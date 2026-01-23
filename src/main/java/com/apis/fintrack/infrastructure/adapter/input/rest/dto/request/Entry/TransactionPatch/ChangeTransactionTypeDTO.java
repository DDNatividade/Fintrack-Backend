package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.TransactionPatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTransactionTypeDTO {
    private boolean income;
}
