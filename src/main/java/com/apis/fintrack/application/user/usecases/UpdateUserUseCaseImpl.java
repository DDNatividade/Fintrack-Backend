package com.apis.fintrack.application.user.usecases;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.user.exception.EmailAlreadyExistsException;
import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.domain.user.model.BirthDate;
import com.apis.fintrack.domain.user.model.Email;
import com.apis.fintrack.domain.user.model.Password;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.domain.user.port.input.UpdateUserUseCase;
import com.apis.fintrack.domain.user.port.output.PasswordEncoderPort;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ImplementaciÃ³n del caso de uso de actualizaciÃ³n de usuario.
 * 
 * Proporciona mÃ©todos para actualizar diferentes campos del usuario,
 * aplicando las reglas de negocio correspondientes.
 */
@Service
@Transactional
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    
    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository, 
                                 PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User updateName(Long userId, String newName) {
        User user = findUserOrThrow(userId);
        user.changeName(newName);
        return userRepository.save(user);
    }
    
    @Override
    public User updateSurname(Long userId, String newSurname) {
        User user = findUserOrThrow(userId);
        user.changeSurname(newSurname);
        return userRepository.save(user);
    }
    
    @Override
    public User updateEmail(Long userId, String newEmail) {
        User user = findUserOrThrow(userId);
        
        // Verify that the new email is not in use by another user
        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }
        
        user.changeEmail(Email.of(newEmail));
        return userRepository.save(user);
    }
    
    @Override
    public User updatePassword(Long userId, String newPassword) {
        User user = findUserOrThrow(userId);
        
        // Hash the new password
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(Password.fromStorage(hashedPassword));
        
        return userRepository.save(user);
    }
    
    @Override
    public User updateBirthDate(Long userId, LocalDate newBirthDate) {
        User user = findUserOrThrow(userId);
        user.changeBirthDate(BirthDate.of(newBirthDate));
        return userRepository.save(user);
    }
    
    @Override
    public User updateRole(Long userId, RoleType newRole) {
        User user = findUserOrThrow(userId);
        user.changeRole(newRole);
        return userRepository.save(user);
    }
    
    @Override
    public User addFunds(Long userId, BigDecimal amount) {
        User user = findUserOrThrow(userId);
        user.addFunds(Money.of(amount));
        return userRepository.save(user);
    }
    
    @Override
    public User withdrawFunds(Long userId, BigDecimal amount) {
        User user = findUserOrThrow(userId);
        user.withdrawFunds(Money.of(amount));
        return userRepository.save(user);
    }
    
    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
    }
}



