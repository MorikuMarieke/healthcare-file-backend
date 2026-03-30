package com.moriku.healthcare_file_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth

            // ---- Public auth endpoints ----
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/invite/accept").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/error/**").permitAll()

            // ---- Users (ADMIN only) ----
            .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")

            // =====================================================
            // SUBRESOURCES FIRST (most specific paths)
            // =====================================================

            // ---- Care plan via client profile ----
            .requestMatchers(HttpMethod.GET, "/client-profiles/*/care-plan")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/care-plan")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // ---- Contact details via client profile ----
            .requestMatchers(HttpMethod.GET, "/client-profiles/*/contact-details")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/contact-details")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // ---- Goals via client profile ----
            .requestMatchers(HttpMethod.POST, "/client-profiles/*/care-plan/goals")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.GET, "/client-profiles/*/care-plan/goals/**")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PUT, "/client-profiles/*/care-plan/goals/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.DELETE, "/client-profiles/*/care-plan/goals/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // =====================================================
            // ROOT RESOURCES AFTER SUBRESOURCES
            // =====================================================

            // ---- Client profiles ----
            .requestMatchers(HttpMethod.POST, "/client-profiles")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.GET, "/client-profiles")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.GET, "/client-profiles/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/care-team/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.DELETE, "/client-profiles/*")
            .hasAuthority("ADMIN")

            // ---- Employee profiles ----
            .requestMatchers(HttpMethod.GET, "/employee-profiles/**")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.PATCH, "/employee-profiles/**")
            .hasAuthority("ADMIN")

            // =====================================================
            // REPORTS
            // =====================================================

            // Staff and admin may view report photos
            .requestMatchers(HttpMethod.GET, "/reports/*/photos")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Staff and admin may view single photo content
            .requestMatchers(HttpMethod.GET, "/reports/*/photos/*/content")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Staff and admin may view single photo metadata
            .requestMatchers(HttpMethod.GET, "/reports/*/photos/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Staff and admin may view reports overview
            .requestMatchers(HttpMethod.GET, "/reports")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Staff and admin may view reports by care plan
            .requestMatchers(HttpMethod.GET, "/reports/care-plans/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Staff and admin may view single report
            .requestMatchers(HttpMethod.GET, "/reports/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // Employees may create/update/delete reports
            .requestMatchers(HttpMethod.POST, "/reports/**")
            .hasAuthority("EMPLOYEE")

            .requestMatchers(HttpMethod.PUT, "/reports/**")
            .hasAuthority("EMPLOYEE")

            .requestMatchers(HttpMethod.DELETE, "/reports/**")
            .hasAuthority("EMPLOYEE")

            // =====================================================
            // ME ENDPOINTS
            // =====================================================

            .requestMatchers(HttpMethod.GET, "/me")
            .authenticated()

            .requestMatchers(HttpMethod.GET, "/me/client-profile")
            .hasAuthority("CLIENT")

            .requestMatchers(HttpMethod.GET, "/me/client-profile/contact-details")
            .hasAuthority("CLIENT")

            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan")
            .hasAuthority("CLIENT")

            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan/goals/**")
            .hasAuthority("CLIENT")

            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan/reports/**")
            .hasAuthority("CLIENT")

            .requestMatchers(HttpMethod.GET, "/me/employee-profile")
            .hasAnyAuthority("EMPLOYEE", "ADMIN")

            .requestMatchers(HttpMethod.PATCH, "/me/password")
            .authenticated()

            .requestMatchers("/me/**")
            .authenticated()

            // =====================================================
            // CARE TEAMS CRUD
            // =====================================================

            .requestMatchers(HttpMethod.POST, "/care-teams")
            .hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.PUT, "/care-teams/*")
            .hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.DELETE, "/care-teams/*")
            .hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.GET, "/care-teams")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            .requestMatchers(HttpMethod.GET, "/care-teams/*")
            .hasAnyAuthority("ADMIN", "EMPLOYEE")

            // ---- Everything else requires authentication ----
            .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        PasswordEncoder passwordEncoder,
        CustomUserDetailsService customUserDetailsService
    ) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(auth);
    }
}