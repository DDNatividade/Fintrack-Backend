package com.apis.fintrack.application.user.usecases;

import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.domain.user.port.input.DeleteUserUseCase;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de eliminación de usuario.
 * 
 * Proporciona métodos para eliminar usuarios del sistema.
 */
@Service
@Transactional
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    
    private final UserRepositoryPort userRepository;
    
    public DeleteUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public void deleteById(Long userId) {
        // Verificar que el usuario existe antes de eliminar
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id " + userId);
        }
        userRepository.deleteById(userId);
    }
    
    @Override
    public void deleteByEmail(String email) {
        // Verificar que el usuario existe antes de eliminar
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("User not found with email" + email);
        }
        userRepository.deleteByEmail(email);
    }
}



