package com.apis.fintrack.Security;

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
        // Excluir rutas de autenticación y Swagger/Documentación
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
            // No hay token, dejamos que siga (puede ser ruta pública)
            filterChain.doFilter(request, response);
            return;
        }

        // EXTRAER el token (quitamos "Bearer " del principio)
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        // VALIDAMOS que:
        //    - Tenemos un username
        //    - NO hay ya una autenticación en el contexto (no está ya autenticado)

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // CARGAR los detalles del usuario desde la BD
            UserDetails userDetails = this.userDetailsService
                    .loadUserByUsername(userEmail);



            // 7. VALIDAR que el token es válido (no expirado, firma correcta, username coincide)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. CREAR el objeto de autenticación
                // Este es el objeto que Spring Security usa para saber "quién eres"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,           // Principal (el usuario)
                                null,                  // Credentials (no las necesitamos ya)
                                userDetails.getAuthorities());// Roles/permisos

                // 9. AÑADIR detalles de la petición (IP, etc)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. GUARDAR la autenticación en el contexto de Spring Security
                // A partir de aquí, Spring sabe que estás autenticado
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. CONTINUAR con la cadena de filtros
        // Si todo fue bien, la petición llegará al controller
        filterChain.doFilter(request, response);



    }
}