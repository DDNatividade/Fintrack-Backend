package com.apis.fintrack.application.transaction;

import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.transaction.port.input.CreateTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
import com.apis.fintrack.domain.user.model.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImplementaciÃ³n del caso de uso de creaciÃ³n de transacciÃ³n.
 * 
 * Aplica las reglas de negocio:
 * - El monto no puede ser cero
 * - Los gastos se almacenan con valor negativo
 * - Los ingresos se almacenan con valor positivo
 * - Solo puede tener una categorÃ­a
 */
@Service
@Transactional
public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {
    
    private final TransactionRepositoryPort transactionRepository;
    
    public CreateTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public Transaction execute(CreateTransactionCommand command) {
        // 1. Validate and create Value Objects (validations are in the VOs)
        Description description = Description.of(command.description());
        
        // 2. Create the amount with the correct sign according to type
        TransactionType type = TransactionType.fromBoolean(command.isIncome());
        TransactionAmount amount = TransactionAmount.of(command.amount(), type);
        
        // 3. Create the transaction date
        TransactionDate transactionDate = command.transactionDate() != null
            ? TransactionDate.of(command.transactionDate())
            : TransactionDate.now();
        
        // 4. Create the category (only ONE)
        Category category = Category.of(command.category());
        
        // 5. Crear la entidad de dominio
        Transaction newTransaction = new Transaction(
            TransactionId.empty(),
            description,
            amount,
            transactionDate,
            category,
            UserId.of(command.userId())
        );
        
        // 6. Persistir y devolver con ID asignado
        return transactionRepository.save(newTransaction);
    }
}



