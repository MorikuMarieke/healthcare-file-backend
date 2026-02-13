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

//    @Bean //Turn this on and securityfilterchain off to test without security
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
//
//        http.csrf(csrf -> csrf.disable());
//
//        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.authorizeHttpRequests(auth -> auth
//            .anyRequest().permitAll()
//        );
//
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth

            // ---- Public auth endpoints ----
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/invite/accept").permitAll()

            // ---- Users (staff accounts) ----
            // POST /users -> ADMIN creates EMPLOYEE or ADMIN
            .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")

            // Optional: user management endpoints
            .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/users/**").hasAuthority("ADMIN")

            // ---- Client profiles (dossiers) ----
            // staff creates client file
            .requestMatchers(HttpMethod.POST, "/client-profiles").hasAnyAuthority("ADMIN", "EMPLOYEE")

            // staff updates client file or contact email
            .requestMatchers(HttpMethod.PATCH, "/client-profiles/**").hasAnyAuthority("ADMIN", "EMPLOYEE")

            // staff can view client files (choose your preference)
            .requestMatchers(HttpMethod.GET, "/client-profiles/**").hasAnyAuthority("ADMIN", "EMPLOYEE")

            // staff deletes client files (usually ADMIN only)
            .requestMatchers(HttpMethod.DELETE, "/client-profiles/**").hasAuthority("ADMIN")

            // ---- Employee profiles ----
            // If only staff should edit employee profiles:
            .requestMatchers(HttpMethod.PATCH, "/employee-profiles/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/employee-profiles/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/me/client-profile").hasAuthority("CLIENT")
            .requestMatchers(HttpMethod.GET, "/me/employee-profile").hasAnyAuthority("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/me/password").authenticated()
            .requestMatchers("/me/**").authenticated()

            // ---- Care teams ----
            .requestMatchers(HttpMethod.POST, "/care-teams/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/care-teams/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/care-teams/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/care-teams/**").hasAnyAuthority("ADMIN", "EMPLOYEE")

            // ---- Everything else requires a valid token ----
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
