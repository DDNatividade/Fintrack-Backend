package com.apis.fintrack.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Exclude authentication routes and Swagger/Documentation
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
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token, let it pass (could be a public route)
            filterChain.doFilter(request, response);
            return;
        }

        // EXTRACT the token (remove "Bearer " from the beginning)
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        // VALIDATE that:
        //    - We have a username
        //    - There is NO already an authentication in the context (not already authenticated)

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // LOAD user details from DB
            UserDetails userDetails = this.userDetailsService
                    .loadUserByUsername(userEmail);



            // 7. VALIDATE that the token is valid (not expired, correct signature, username matches)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. CREATE the authentication object
                // This is the object Spring Security uses to know "who you are"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,           // Principal (the user)
                                null,                  // Credentials (not needed anymore)
                                userDetails.getAuthorities());// Roles/permissions

                // 9. ADD request details (IP, etc)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. SAVE the authentication in the Spring Security context
                // From here on, Spring knows you are authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. CONTINUE with the filter chain
        // If everything went well, the request will reach the controller
        filterChain.doFilter(request, response);



    }
}

