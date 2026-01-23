package com.apis.fintrack.infrastructure.security.service;

import com.apis.fintrack.domain.user.exception.RolesNotFoundException;
import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.user.exception.UserNotFoundException;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.CreateUserDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserLoginDTO;
import com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit.AuthResponse;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.UserMapperService;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.RoleRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import com.apis.fintrack.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserMapperService userMapper;

    /**
     * REGISTRO - Crea usuario y devuelve JWT
     */
    public AuthResponse register(CreateUserDTO request) {
        // 1. Convertir DTO a entidad JPA
        var newUser = userMapper.toUserEntity(request);

        // 2. Hashear password y asignar rol
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(roleRepository.findByRoleName(RoleType.USER)
                .orElseThrow(() -> new RolesNotFoundException("Role not found")));

        // 3. Guardar en BD
        userRepository.save(newUser);

        // 4. Generar JWT
        var jwtToken = jwtService.generateToken(newUser);

        // 5. Devolver respuesta con el token
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * LOGIN - Validates credentials and returns JWT
     */
    public AuthResponse authenticate(UserLoginDTO request) {
        // 1. AUTHENTICATE - Here Spring validates user/password
        // If it fails, it throws an exception automatically
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. If we reach here, the credentials are correct
        // Find the user in DB
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 3. Generate JWT
        var jwtToken = jwtService.generateToken(user);

        // 4. Return response with the token
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}


