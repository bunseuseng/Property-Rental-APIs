package com.group5.rental_room.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.group5.rental_room.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity // <--- Add this to enable @PreAuthorize
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(request -> request
                            // 1. SPECIFIC Public Endpoints (No wildcards for sensitive areas)
                            .requestMatchers(
                                "/api/v1/auth-service/register", 
                                "/api/v1/auth-service/authenticate",
                                "/instances"
                            ).permitAll()
                            
                            .requestMatchers("/api/v1/auth-service/assign-role").authenticated()

                            // 3. Catch-all for everything else
                            .anyRequest().authenticated())

                    .sessionManagement(manager -> manager
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

}