package com.apis.fintrack.infrastructure.security.adapter;

import com.apis.fintrack.application.auth.port.output.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that encapsulates access to SecurityContextHolder and exposes a
 * minimal, testable CurrentUserPort to the application.
 */
@Component
public final class SecurityContextCurrentUserAdapter implements CurrentUserPort {

    @Override
    public Optional<UUID> currentUserId() {
        return currentUsername().map(u -> UUID.nameUUIDFromBytes(u.getBytes()));
    }

    @Override
    public Optional<String> currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        }
        if (principal instanceof String) {
            return Optional.of((String) principal);
        }
        return Optional.empty();
    }
}

