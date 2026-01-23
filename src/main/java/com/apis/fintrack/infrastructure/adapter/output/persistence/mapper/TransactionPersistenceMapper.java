package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.user.model.UserId;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.TransactionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.util.Objects;

/**
 * Mapper para convertir entre Transaction (dominio) y TransactionJPAEntity (JPA).
 * 
 * Responsabilidades:
 * - Convertir entidades JPA a entidades de dominio
 * - Convertir entidades de dominio a entidades JPA
 * - Actualizar entidades JPA existentes con datos del dominio
 */
@Component
public class TransactionPersistenceMapper {
    
    private final UserRepository userRepository;
    
    public TransactionPersistenceMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * 
     * @param jpaEntity la entidad JPA
     * @return la entidad de dominio
     */
    public Transaction toDomain(TransactionJPAEntity jpaEntity) {
        Objects.requireNonNull(jpaEntity, "La entidad JPA no puede ser nula");

        return new Transaction(
            TransactionId.of(jpaEntity.getId()),
            Description.of(jpaEntity.getDescription()),
            TransactionAmount.fromStorage(jpaEntity.getAmount()),
            TransactionDate.fromStorage(jpaEntity.getTransaction_date()),
            Category.of(jpaEntity.getCategory()),
            UserId.of(jpaEntity.getUser().getUserId())
        );
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA.
     * 
     * @param domainTransaction la entidad de dominio
     * @return la entidad JPA
     */
    public TransactionJPAEntity toJpaEntity(Transaction domainTransaction) {
       Objects.requireNonNull(domainTransaction, "La entidad de dominio no puede ser nula");

        TransactionJPAEntity jpaEntity = new TransactionJPAEntity();
        
        // Solo asignar ID si existe (no es nueva)
        if (!domainTransaction.getId().isEmpty()) {
            jpaEntity.setId(domainTransaction.getId().getValue());
        }
        
        jpaEntity.setDescription(domainTransaction.getDescription().getValue());
        jpaEntity.setAmount(domainTransaction.getAmount().getValue());
        jpaEntity.setTransaction_date(domainTransaction.getTransactionDate().getValue());
        jpaEntity.setCategory(domainTransaction.getCategory().getValue());
        jpaEntity.setIncome(domainTransaction.isIncome());
        
        // Buscar el usuario
        UserJPAEntity userEntity = userRepository.searchById(domainTransaction.getId().getValue())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe usuario con ID: " + domainTransaction.getId().toString()
            ));
        jpaEntity.setUser(userEntity);
        
        return jpaEntity;
    }
    
    /**
     * Actualiza una entidad JPA existente con los datos del dominio.
     * 
     * @param domainTransaction la entidad de dominio con los nuevos datos
     * @param jpaEntity la entidad JPA a actualizar
     */
    public void updateJpaEntity(Transaction domainTransaction, TransactionJPAEntity jpaEntity) {
        Objects.requireNonNull(domainTransaction, "La entidad de dominio no puede ser nula");
        Objects.requireNonNull(jpaEntity, "La entidad JPA no puede ser nula");

        jpaEntity.setDescription(domainTransaction.getDescription().getValue());
        jpaEntity.setAmount(domainTransaction.getAmount().getValue());
        jpaEntity.setTransaction_date(domainTransaction.getTransactionDate().getValue());
        jpaEntity.setCategory(domainTransaction.getCategory().getValue());
        jpaEntity.setIncome(domainTransaction.isIncome());
    }
}


