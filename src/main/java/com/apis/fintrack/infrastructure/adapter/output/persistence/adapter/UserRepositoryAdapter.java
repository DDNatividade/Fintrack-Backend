package com.apis.fintrack.infrastructure.adapter.output.persistence.adapter;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto de salida UserRepositoryPort.
 * 
 * Este adaptador traduce las operaciones del dominio a operaciones de Spring Data JPA,
 * usando el UserPersistenceMapper para convertir entre entidades de dominio y JPA.
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserRepository userRepository;
    private final UserPersistenceMapper mapper;
    
    public UserRepositoryAdapter(UserRepository userRepository, UserPersistenceMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    
    // ==================== OPERACIONES DE LECTURA ====================
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.searchById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByNameAndSurname(String name, String surname) {
        return userRepository.findByUsernameAndSurname(name, surname)
                .map(mapper::toDomain);
    }
    
    @Override
    public Page<User> findByRole(RoleType role, Pageable page) {
      return userRepository.findByRole(role, page)
              .map(mapper::toDomain);
    }
    
    @Override
    public Page<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<UserJPAEntity> pageResult = userRepository.findByDateBetween(startDate, endDate, pageable);
        Page<User> userPage = pageResult.map(mapper::toDomain);
        return userPage;

    }

    @Override
    public Page<User> findAll(Pageable page) {
        Page<UserJPAEntity> pageResult = userRepository.showAll(page);
        Page<User> userPage = pageResult.map(mapper::toDomain);
        return userPage;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return userRepository.count();
    }
    
    // ==================== OPERACIONES DE ESCRITURA ====================
    
    @Override
    public User save(User user) {
        UserJPAEntity jpaEntity;
        
        // Si el usuario tiene ID, es un update
        if (!user.getId().isEmpty()) {
            // Buscar la entidad existente para mantener las relaciones
            jpaEntity = userRepository.searchById(user.getId().getValue())
                    .orElseGet(() -> mapper.toJpaEntity(user));
            mapper.updateJpaEntity(user, jpaEntity);
        } else {
            // Es un nuevo usuario
            jpaEntity = mapper.toJpaEntity(user);
        }
        
        UserJPAEntity savedEntity = userRepository.save(jpaEntity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public void deleteByEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> userRepository.deleteById(user.getUserId()));
    }
}


