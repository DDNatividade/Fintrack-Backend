package com.apis.fintrack.application.user.usecases;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.input.FindUserUseCase;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementación del caso de uso de búsqueda de usuarios.
 *
 * Proporciona diferentes métodos de búsqueda delegando al puerto de salida.
 */
@Service
@Transactional(readOnly = true)
public class FindUserUseCaseImpl implements FindUserUseCase {
    
    private final UserRepositoryPort userRepository;
    
    public FindUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
    }
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
    }
    
    @Override
    public User findByNameAndSurname(String name, String surname) {
        return userRepository.findByNameAndSurname(name, surname)
                .orElseThrow(() -> new UserNotFoundException(
                    "Usuario no encontrado: " + name + " " + surname));
    }

    @Override
    public Page<User> findByRole(RoleType role, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : PageRequest.of(0,20);
        return userRepository.findByRole(role, effective);
    }

    @Override
    public Page<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable effective = pageable != null ? pageable : PageRequest.of(0,20);
        return userRepository.findByBirthDateBetween(startDate, endDate, effective);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Pageable effective = pageable != null ? pageable : PageRequest.of(0,20);
        return userRepository.findAll(effective);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
