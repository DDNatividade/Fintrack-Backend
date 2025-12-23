package com.apis.fintrack.application.userCasesImpl.user;

import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.domain.user.port.input.DeleteUserUseCase;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImplementaciÃ³n del caso de uso de eliminaciÃ³n de usuario.
 * 
 * Proporciona mÃ©todos para eliminar usuarios del sistema.
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
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
    
    @Override
    public void deleteByEmail(String email) {
        // Verificar que el usuario existe antes de eliminar
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("Usuario no encontrado con email: " + email);
        }
        userRepository.deleteByEmail(email);
    }
}



