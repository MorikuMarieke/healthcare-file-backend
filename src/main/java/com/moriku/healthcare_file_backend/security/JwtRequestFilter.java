package com.moriku.healthcare_file_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(

        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain

    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No token: proceed as anonymous (SecurityConfig decides)
        if (authHeader == null || authHeader.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        // Token present but not Bearer: treat as invalid auth attempt
        if (!authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid Authorization header\"}");
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            String email = jwtUtil.extractEmail(jwt);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (!jwtUtil.validateToken(jwt, userDetails)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            chain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid token\"}");
        }
    }

}
