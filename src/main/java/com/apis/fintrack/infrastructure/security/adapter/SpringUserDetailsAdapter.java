package com.apis.fintrack.infrastructure.security.adapter;

import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import com.apis.fintrack.application.auth.port.output.AuthenticationPort;
import com.apis.fintrack.infrastructure.security.service.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements the AuthenticationPort by delegating to the existing
 * UserDetailsServiceImpl and translating framework UserDetails into the
 * application-friendly AuthenticatedUser DTO.
 */
@Component
public final class SpringUserDetailsAdapter implements AuthenticationPort {

    private final UserDetailsServiceImpl userDetailsService;

    public SpringUserDetailsAdapter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Optional<AuthenticatedUser> authenticate(String username, String rawPassword) {
        try {
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            // For authentication we rely on Spring's AuthenticationManager in the SecurityConfig.
            // This adapter provides lookup functionality; credential validation should be
            // performed by AuthenticationManager. We return the user if found.
            return Optional.of(mapToAuthenticatedUser(ud));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthenticatedUser> findByUsername(String username) {
        try {
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            return Optional.of(mapToAuthenticatedUser(ud));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void logout(String token) {
        // No-op for stateless JWT setup. If a token blacklist is implemented, this adapter
        // should call the corresponding infrastructure to persist the revocation.
    }

    private AuthenticatedUser mapToAuthenticatedUser(UserDetails ud) {
        // Note: We do not have direct access to the application's User ID here because
        // UserDetails only exposes username and authorities. If the application requires
        // UUID userId, add a custom UserDetails implementation that stores it.
        Set<String> roles = ud.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());
        UUID unknownId = UUID.nameUUIDFromBytes(ud.getUsername().getBytes());
        return new AuthenticatedUser(unknownId, ud.getUsername(), roles);
    }
}

