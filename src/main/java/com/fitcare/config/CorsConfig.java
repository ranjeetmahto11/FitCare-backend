package com.fitcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation
        .Configuration;
import org.springframework.web.cors
        .CorsConfiguration;
import org.springframework.web.cors
        .UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config =
                new CorsConfiguration();

        // ✅ Allow all origins
        config.setAllowedOriginPatterns(
                List.of("*"));

        // ✅ Allow all methods
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT",
                "DELETE", "OPTIONS", "PATCH"));

        // ✅ Allow all headers
        config.setAllowedHeaders(
                List.of("*"));

        // ✅ Expose Authorization header
        config.setExposedHeaders(
                List.of("Authorization",
                        "Content-Type"));

        // ✅ Allow credentials
        config.setAllowCredentials(true);

        // ✅ Cache preflight 1 hour
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
                "/**", config);

        return new CorsFilter(source);
    }
}