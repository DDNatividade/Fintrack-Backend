package com.apis.fintrack.infrastructure.security;

import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import com.apis.fintrack.application.auth.port.output.AuthenticationPort;
import com.apis.fintrack.application.auth.port.output.TokenPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtAdaptersTest {

    @Test
    void tokenPortAndAuthPortCanBeMocked() {
        TokenPort tokenPort = Mockito.mock(TokenPort.class);
        AuthenticationPort authPort = Mockito.mock(AuthenticationPort.class);

        AuthenticatedUser user = new AuthenticatedUser(UUID.randomUUID(), "user@example.com", Set.of("ROLE_USER"));

        Mockito.when(tokenPort.validateAndExtractSubject("valid-token")).thenReturn(java.util.Optional.of("user@example.com"));
        Mockito.when(authPort.findByUsername("user@example.com")).thenReturn(java.util.Optional.of(user));

        java.util.Optional<String> subject = tokenPort.validateAndExtractSubject("valid-token");
        assertTrue(subject.isPresent());
        assertEquals("user@example.com", subject.get());

        java.util.Optional<AuthenticatedUser> maybeUser = authPort.findByUsername(subject.get());
        assertTrue(maybeUser.isPresent());
        assertEquals(user.getUsername(), maybeUser.get().getUsername());
    }
}

