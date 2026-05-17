package com.fitcare.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context
        .SecurityContextHolder;
import org.springframework.security.core.userdetails
        .UserDetails;
import org.springframework.security.core.userdetails
        .UserDetailsService;
import org.springframework.security.web.authentication
        .WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter
        .OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService
            userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Skip public endpoints
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs")
                || path.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Get Authorization header
        final String authHeader =
                request.getHeader("Authorization");

        // 3. No token — continue without auth
        if (authHeader == null
                || !authHeader.startsWith(
                "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Extract JWT
        final String jwt =
                authHeader.substring(7);
        String userEmail = null;

        try {
            userEmail =
                    jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            logger.warn("Invalid JWT: "
                    + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Authenticate if not already done
        if (userEmail != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            UserDetails userDetails = null;

            try {
                userDetails = userDetailsService
                        .loadUserByUsername(userEmail);
            } catch (Exception e) {
                logger.warn("User not found: "
                        + e.getMessage());
                filterChain.doFilter(
                        request, response);
                return;
            }

            // 6. Validate and set auth
            if (jwtUtil.isTokenValid(
                    jwt, userDetails)) {

                UsernamePasswordAuthenticationToken
                        authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails
                                        .getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}