package com.apis.fintrack.application.auth.port.output;

import java.util.Optional;
import java.util.UUID;

/**
 * Port to obtain information about the currently authenticated user.
 * Implementations typically adapt from framework-specific contexts (SecurityContextHolder).
 */
public interface CurrentUserPort {

    /**
     * Obtain the current authenticated user's id if present.
     */
    Optional<UUID> currentUserId();

    /**
     * Obtain the current authenticated username if present.
     */
    Optional<String> currentUsername();

}

