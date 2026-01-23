package com.apis.fintrack.application.user.usecases;

import com.apis.fintrack.domain.user.exception.EmailAlreadyExistsException;
import com.apis.fintrack.domain.user.model.*;
import com.apis.fintrack.domain.user.port.input.RegisterUserUseCase;
import com.apis.fintrack.domain.user.port.output.PasswordEncoderPort;
import com.apis.fintrack.domain.user.port.output.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImplementaciÃ³n del caso de uso de registro de usuario.
 * 
 * Orquesta la lÃ³gica de negocio para registrar nuevos usuarios,
 * delegando la persistencia al puerto de salida y el hashing al PasswordEncoderPort.
 */
@Service
@Transactional
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    
    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, 
                                   PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User execute(RegisterUserCommand command) {
        // 1. Validate that the email is not in use
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyExistsException(command.email());
        }
        
        // 2. Hash the password
        String hashedPassword = passwordEncoder.encode(command.password());
        
        // 3. Create the domain entity with Value Objects
        User newUser = User.create(
            FullName.of(command.name(), command.surname()),
            Email.of(command.email()),
            Password.fromStorage(hashedPassword),  // Already hashed
            BirthDate.of(command.birthDate())
        );
        
        // 4. Persist and return the user with assigned ID
        return userRepository.save(newUser);
    }
}



