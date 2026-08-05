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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                // Enable CORS and disable CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                       // .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        //.requestMatchers("/api/auth/register/**").hasAnyRole("ADMIN", "LAB_STAFF", "TECHNICIAN")
                        .requestMatchers("/api/auth/register/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/laboratories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/laboratories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/laboratories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/laboratories/**").hasAnyRole("ADMIN", "LAB_STAFF", "TECHNICIAN")

                        .requestMatchers(HttpMethod.POST, "/api/equipment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/equipment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/equipment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/equipment/**").hasAnyRole("ADMIN", "LAB_STAFF", "TECHNICIAN")

                        .requestMatchers(HttpMethod.POST, "/laboratory-staff").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/laboratory-staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/laboratory-staff/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/laboratory-staff").hasAnyRole("ADMIN", "LAB_STAFF")
                        .requestMatchers(HttpMethod.GET, "/laboratory-staff/**").hasAnyRole("ADMIN", "LAB_STAFF")

                        .requestMatchers(HttpMethod.POST, "/api/technician/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/technician/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/technician/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/technician/**").hasAnyRole("ADMIN", "TECHNICIAN")

                        .requestMatchers(HttpMethod.PUT, "/reservations/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/reservations/*/cancel").hasAnyRole("ADMIN", "LAB_STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/reservations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reservations/**").hasAnyRole("ADMIN", "LAB_STAFF")
                        .requestMatchers(HttpMethod.GET, "/reservations/**").hasAnyRole("ADMIN", "LAB_STAFF")

                        .requestMatchers(HttpMethod.PUT, "/api/maintenances/*/complete").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/maintenances/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/maintenances/**").hasAnyRole("ADMIN", "TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/maintenances/**").hasAnyRole("ADMIN", "TECHNICIAN")

                        .requestMatchers("/api/report/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from your frontend environment/origins
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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