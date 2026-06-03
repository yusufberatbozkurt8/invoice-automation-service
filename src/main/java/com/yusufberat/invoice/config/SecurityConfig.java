package com.yusufberat.invoice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityProperties properties) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny));

        if (!properties.isEnabled()) {
            return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
        }

        if (!properties.isConfigured()) {
            throw new IllegalStateException(
                    "app.security.api-key tanımlı değil. Ortam değişkeni API_KEY ayarlayın veya dev profili kullanın."
            );
        }

        ApiKeyAuthFilter apiKeyFilter = new ApiKeyAuthFilter(properties);
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").denyAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
