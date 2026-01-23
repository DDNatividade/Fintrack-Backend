package com.apis.fintrack.domain.user.service;

import com.apis.fintrack.domain.user.exception.EmailAlreadyExistsException;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;

public class UserDuplicationService {
    private final UserRepository userRepository;

    public UserDuplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkIfEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
    }
}

