package com.moriku.healthcare_file_backend.security;

import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.getUserEntityByEmail(email);

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean accountNonLocked = true;

        Instant changedAt = user.getPasswordChangedAt();
        boolean credentialsNonExpired = true;

        if (user.hasRole("EMPLOYEE") || user.hasRole("ADMIN")) {
            if (changedAt == null) {
                credentialsNonExpired = false;
            } else {
                Instant expiry = changedAt.plus(90, ChronoUnit.DAYS);
                credentialsNonExpired = Instant.now().isBefore(expiry);
            }
        }
        SimpleGrantedAuthority authority =
            new SimpleGrantedAuthority(user.getRole().getName());

        List<SimpleGrantedAuthority> authorities =
            List.of(authority);

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        );
    }
}