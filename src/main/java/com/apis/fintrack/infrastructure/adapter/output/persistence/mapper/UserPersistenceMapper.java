package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.user.model.*;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.RoleJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.RoleRepository;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad de dominio User y la entidad JPA UserJPAEntity.
 * 
 * Este mapper es parte de la capa de infraestructura y conoce tanto el dominio
 * como la persistencia, actuando como traductor entre ambos mundos.
 */
@Component
public class UserPersistenceMapper {
    
    private final RoleRepository roleRepository;
    
    public UserPersistenceMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * 
     * @param jpaEntity la entidad JPA
     * @return la entidad de dominio
     */
    public User toDomain(UserJPAEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        return new User(
            UserId.of(jpaEntity.getUserId()),
            FullName.of(jpaEntity.getName(), jpaEntity.getSurname()),
            Email.of(jpaEntity.getEmail()),
            Password.fromStorage(jpaEntity.getPassword()),
            BirthDate.ofUnchecked(jpaEntity.getBirthDate()),
            Money.of(jpaEntity.getAvailableFunds())
        );
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA.
     * 
     * @param domainUser la entidad de dominio
     * @return la entidad JPA
     */
    public UserJPAEntity toJpaEntity(User domainUser) {
        if (domainUser == null) {
            return null;
        }
        
        UserJPAEntity jpaEntity = new UserJPAEntity();
        
        // Solo asignar ID si existe (para updates)
        if (!domainUser.getId().isEmpty()) {
            jpaEntity.setUserId(domainUser.getId().getValue());
        }
        
        jpaEntity.setName(domainUser.getFullName().getName());
        jpaEntity.setSurname(domainUser.getFullName().getSurname());
        jpaEntity.setEmail(domainUser.getEmail().getValue());
        jpaEntity.setPassword(domainUser.getPassword().getValue());
        jpaEntity.setBirthDate(domainUser.getBirthDate().getValue());
        jpaEntity.setAvailableFunds(domainUser.getAvailableFunds().getAmount());
        
        // Buscar el rol en la BD
        RoleJPAEntity roleEntity = roleRepository.findByRoleName(domainUser.getRole())
                .orElseGet(() -> {
                    RoleJPAEntity defaultRole = new RoleJPAEntity();
                    defaultRole.setRoleName(RoleType.USER);
                    return defaultRole;
                });
        jpaEntity.setRole(roleEntity);
        
        return jpaEntity;
    }
    
    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio.
     * Ãštil para operaciones de update donde queremos mantener las relaciones JPA.
     * 
     * @param domainUser la entidad de dominio con los nuevos datos
     * @param jpaEntity la entidad JPA a actualizar
     */
    public void updateJpaEntity(User domainUser, UserJPAEntity jpaEntity) {
        if (domainUser == null || jpaEntity == null) {
            return;
        }
        
        jpaEntity.setName(domainUser.getFullName().getName());
        jpaEntity.setSurname(domainUser.getFullName().getSurname());
        jpaEntity.setEmail(domainUser.getEmail().getValue());
        jpaEntity.setPassword(domainUser.getPassword().getValue());
        jpaEntity.setBirthDate(domainUser.getBirthDate().getValue());
        jpaEntity.setAvailableFunds(domainUser.getAvailableFunds().getAmount());
        
        // Update role if changed
        if (jpaEntity.getRole() == null ||
            !jpaEntity.getRole().getRoleName().equals(domainUser.getRole())) {
            RoleJPAEntity roleEntity = roleRepository.findByRoleName(domainUser.getRole())
                    .orElseThrow(() -> new IllegalStateException(
                        "Role not found: " + domainUser.getRole()));
            jpaEntity.setRole(roleEntity);
        }
    }
}


