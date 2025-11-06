package com.apis.fintrack.DTO.TransactionEntity.Entry.Patch;

import com.apis.fintrack.Entity.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTransactionCategoryDTO {
    private CategoryEnum category;
}
