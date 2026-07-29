package com.example.HealthcareEquipmentSystem.SecurityConfiguration;

import com.example.HealthcareEquipmentSystem.Services.CustomUserDetailsService;
import com.example.HealthcareEquipmentSystem.Utility.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private CustomUserDetailsService customUserDetailsService;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                // Public authentication endpoints
                                .requestMatchers("/api/auth/**").permitAll()

                                // ADMIN permissions (manages labs, equipment, technicians, reports)
                                .requestMatchers("/laboratories/**").hasRole("ADMIN")
                                .requestMatchers("/technicians/**").hasRole("ADMIN")
                                .requestMatchers("/reports/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/equipment/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/equipment/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/equipment/**").hasRole("ADMIN")
                                .requestMatchers("/reservations/{id}/approve").hasRole("ADMIN")

                        // LAB_STAFF permissions (view equipment, create/cancel reservations,reservation history)
                .requestMatchers(HttpMethod.GET, "/equipment/**").hasAnyRole("ADMIN",
                        "LAB_STAFF", "TECHNICIAN")
                .requestMatchers(HttpMethod.POST, "/reservations").hasRole("LAB_STAFF")
                .requestMatchers("/reservations/{id}/cancel").hasRole("LAB_STAFF")
                .requestMatchers("/reservations/history/**").hasRole("LAB_STAFF")

                // TECHNICIAN permissions (view maintenance, complete maintenance)
                .requestMatchers(HttpMethod.GET, "/maintenance/**").hasAnyRole("ADMIN",
                        "TECHNICIAN")
                .requestMatchers(HttpMethod.POST, "/maintenance/**").hasAnyRole("ADMIN",
                        "TECHNICIAN")
                .requestMatchers("/maintenance/{id}/complete").hasRole("TECHNICIAN")

                // All remaining requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
