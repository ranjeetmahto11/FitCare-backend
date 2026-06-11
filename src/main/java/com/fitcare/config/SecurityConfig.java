package com.fitcare.config;

import com.fitcare.security.JwtAuthFilter;
import com.fitcare.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation
        .Configuration;
import org.springframework.security.authentication
        .AuthenticationManager;
import org.springframework.security.authentication.dao
        .DaoAuthenticationProvider;
import org.springframework.security.config.annotation
        .authentication.configuration
        .AuthenticationConfiguration;
import org.springframework.security.config.annotation
        .method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation
        .web.builders.HttpSecurity;
import org.springframework.security.config.annotation
        .web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation
        .web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http
        .SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt
        .BCryptPasswordEncoder;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.security.web
        .SecurityFilterChain;
import org.springframework.security.web.authentication
        .UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl
            userDetailsService;

    // ✅ Public URLs defined here
    private static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/api/goals/all",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/error"
    };

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                // ✅ Enable CORS
                .cors(cors -> cors.configure(http))

                .headers(headers -> headers
                        .frameOptions(
                                frame -> frame
                                        .sameOrigin()))

                // ✅ HttpServletResponse imported
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req, res, authEx) -> {
                                    res.setContentType(
                                            "application/json");
                                    res.setStatus(
                                            HttpServletResponse
                                                    .SC_UNAUTHORIZED);
                                    res.getWriter().write(
                                            "{\"success\":false,"
                                                    + "\"message\":"
                                                    + "\"Unauthorized."
                                                    + " Please login.\"}");
                                })
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy
                                        .STATELESS))

                // ✅ daoAuthenticationProvider defined
                .authenticationProvider(
                        authenticationProvider())

                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter
                                .class);

        return http.build();
    }


    public DaoAuthenticationProvider authenticationProvider() {
        // Spring Boot 4 — pass UserDetailsService in constructor
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}