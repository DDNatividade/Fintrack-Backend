package com.apis.fintrack.application.userCasesImpl;

import com.apis.fintrack.domain.user.model.role.model.RoleType;
import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.input.FindUserUseCase;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * ImplementaciÃ³n del caso de uso de bÃºsqueda de usuarios.
 * 
 * Proporciona diferentes mÃ©todos de bÃºsqueda delegando al puerto de salida.
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
    public List<User> findByRole(RoleType role, int page, int size) {
        return userRepository.findByRole(role, page, size);
    }
    
    @Override
    public List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, int page, int size) {
        return userRepository.findByBirthDateBetween(startDate, endDate, page, size);
    }
    
    @Override
    public List<User> findAll(int page, int size) {
        return userRepository.findAll(page, size);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}



