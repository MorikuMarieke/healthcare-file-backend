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

            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/invite/accept").permitAll()

            .requestMatchers("/error").permitAll()
            .requestMatchers("/error/**").permitAll()

            .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.GET, "/client-profiles/*/care-plan").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/care-plan").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/client-profiles/*/contact-details").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/contact-details").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/client-profiles/*/care-plan/goals").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/client-profiles/*/care-plan/goals/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PUT, "/client-profiles/*/care-plan/goals/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.DELETE, "/client-profiles/*/care-plan/goals/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/client-profiles").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/client-profiles").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/client-profiles/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PATCH, "/client-profiles/*/care-team/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.DELETE, "/client-profiles/*").hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.GET, "/employee-profiles/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.PATCH, "/employee-profiles/**").hasAuthority("ADMIN")

            .requestMatchers(HttpMethod.GET, "/reports/*/photos").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/reports/*/photos/*/content").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/reports/*/photos/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/reports").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/reports/care-plans/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/reports/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/reports/**").hasAuthority("EMPLOYEE")
            .requestMatchers(HttpMethod.PUT, "/reports/**").hasAuthority("EMPLOYEE")
            .requestMatchers(HttpMethod.DELETE, "/reports/**").hasAuthority("EMPLOYEE")

            .requestMatchers(HttpMethod.GET, "/me").authenticated()
            .requestMatchers(HttpMethod.GET, "/me/client-profile").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/client-profile/contact-details").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan/goals/**").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/client-profile/care-plan/reports/**").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/employee-profile").hasAnyAuthority("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/me/password").authenticated()
            .requestMatchers("/me/**").authenticated()

            .requestMatchers(HttpMethod.POST, "/care-teams").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/care-teams/*").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/care-teams/*").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/care-teams").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/care-teams/*").hasAnyAuthority("ADMIN", "EMPLOYEE")

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