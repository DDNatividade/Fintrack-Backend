package com.apis.fintrack.infrastructure.adapter.input.rest.mapper;

import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.CreateTransactionUseCase.CreateTransactionCommand;
import com.apis.fintrack.domain.transaction.port.input.UpdateTransactionUseCase.UpdateTransactionCommand;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateTransactionDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.ShowTransactionDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DTOs REST y entidades/comandos del dominio.
 */
@Component
public class TransactionRestMapper {
    
    /**
     * Convierte un DTO de creación a un comando del caso de uso.
     * 
     * @param dto el DTO de entrada
     * @param userId el ID del usuario propietario
     * @return el comando para crear la transacción
     */
    public CreateTransactionCommand toCommand(CreateTransactionDTO dto, Long userId) {
        return new CreateTransactionCommand(
            dto.getDescription(),
            dto.getAmount(),
            dto.isIncome(),
            dto.getCategory(),
            LocalDate.now(),
            userId
        );
    }
    
    /**
     * Convierte un DTO de creación a un comando de actualización.
     * 
     * @param transactionId el ID de la transacción a actualizar
     * @param dto el DTO con los nuevos datos
     * @return el comando para actualizar la transacción
     */
    public UpdateTransactionCommand toUpdateCommand(Long transactionId, CreateTransactionDTO dto) {
        return new UpdateTransactionCommand(
            transactionId,
            dto.getDescription(),
            dto.getAmount(),
            dto.isIncome(),
            dto.getCategory(),
            LocalDate.now()
        );
    }
    
    /**
     * Convierte una entidad de dominio a un DTO de respuesta.
     * 
     * @param transaction la entidad de dominio
     * @return el DTO de respuesta
     */
    public ShowTransactionDTO toShowTransactionDTO(Transaction transaction) {
        return new ShowTransactionDTO(
            transaction.getDescription().getValue(),
            transaction.getTransactionDate().getValue(),
            transaction.getAmount().getValue(),
            transaction.getCategory().getValue()
        );
    }
    
    /**
     * Convierte una lista de entidades de dominio a lista de DTOs.
     * 
     * @param transactions lista de entidades
     * @return lista de DTOs
     */
    public List<ShowTransactionDTO> toShowTransactionDTOList(List<Transaction> transactions) {
        return transactions.stream()
            .map(this::toShowTransactionDTO)
            .collect(Collectors.toList());
    }
}


