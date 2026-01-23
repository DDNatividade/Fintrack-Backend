package com.apis.fintrack.infrastructure.security.adapter;

import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import com.apis.fintrack.application.auth.port.output.TokenPort;
import com.apis.fintrack.infrastructure.security.JwtService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Infrastructure adapter that delegates token operations to the existing JwtService.
 * This class keeps token/provider specifics inside infrastructure and exposes a
 * simple, application-friendly contract (TokenPort).
 */
@Component
public final class JwtTokenAdapter implements TokenPort {

    private final JwtService jwtService;

    public JwtTokenAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(AuthenticatedUser user) {
        // Build a minimal UserDetails for JwtService
        UserDetails ud = new User(
                user.getUsername(),
                "", // password not required for token generation
                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        return jwtService.generateToken(ud);
    }

    @Override
    public Optional<String> validateAndExtractSubject(String token) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                return Optional.empty();
            }
            return Optional.of(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        try {
            // JwtService requires UserDetails for validation; create a lightweight one with username
            UserDetails ud = new User(username == null ? "" : username, "", java.util.List.of());
            return jwtService.isTokenValid(token, ud);
        } catch (Exception e) {
            return false;
        }
    }
}
