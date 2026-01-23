package com.apis.fintrack.application.transaction.usecases;

import com.apis.fintrack.application.budget.mapper.TransactionCommandMapper;
import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.transaction.port.input.CreateTransactionUseCase;
import com.apis.fintrack.domain.transaction.port.output.TransactionRepositoryPort;
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
    private final TransactionCommandMapper transactionCommandMapper;

    public CreateTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository, TransactionCommandMapper transactionCommandMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionCommandMapper = transactionCommandMapper;
    }
    
    @Override
    public Transaction execute(CreateTransactionCommand command) {
        // Usar el mapper para construir la entidad de dominio
        Transaction newTransaction = transactionCommandMapper.toTransaction(command);
        // Persistir y devolver con ID asignado
        return transactionRepository.save(newTransaction);
    }
}
