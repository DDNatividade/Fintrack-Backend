package com.apis.fintrack.Mapper;

import com.apis.fintrack.DTO.TransactionEntity.Entry.CreateTransactionDTO;
import com.apis.fintrack.DTO.TransactionEntity.Exit.ShowTransactionDTO;
import com.apis.fintrack.Entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel="Spring")
public interface TransactionMapStruct {

    TransactionEntity toTransactionEntity(CreateTransactionDTO dto);
    ShowTransactionDTO toShowTransactionDTO(TransactionEntity transactionEntity);

}
