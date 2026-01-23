package com.apis.fintrack.infrastructure.security;

import com.apis.fintrack.application.auth.port.output.AuthenticationPort;
import com.apis.fintrack.application.auth.port.output.TokenPort;
import com.apis.fintrack.application.auth.dto.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final TokenPort tokenPort;
    private final AuthenticationPort authenticationPort;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.startsWith("/apis/auth/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/swagger-ui/index.html");

    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = extractJwtFromRequest(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticateIfValid(jwt, request);
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }

    private void authenticateIfValid(String jwt, HttpServletRequest request) {
        if (isAlreadyAuthenticated()) {
            return;
        }

        Optional<String> subject = tokenPort.validateAndExtractSubject(jwt);

        if (subject.isEmpty()) {
            return;
        }

        String username = subject.get();
        Optional<AuthenticatedUser> maybeUser = authenticationPort.findByUsername(username);

        maybeUser.ifPresent(user -> setAuthenticationContext(jwt, user, request));
    }

    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void setAuthenticationContext(String jwt, AuthenticatedUser appUser, HttpServletRequest request) {
        if (!tokenPort.isTokenValid(jwt, appUser.getUsername())) {
            return;
        }

        UserDetails userDetails = buildUserDetails(appUser);
        UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(userDetails, request);

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private UserDetails buildUserDetails(AuthenticatedUser appUser) {
        return new User(
                appUser.getUsername(),
                "",
                appUser.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(
            UserDetails userDetails,
            HttpServletRequest request
    ) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }
}
