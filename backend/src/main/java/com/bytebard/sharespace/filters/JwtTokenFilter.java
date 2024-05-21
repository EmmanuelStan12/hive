package com.bytebard.sharespace.filters;

import com.bytebard.sharespace.config.security.JwtAuthenticationToken;
import com.bytebard.sharespace.config.security.JwtUtil;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final HandlerExceptionResolver exceptionResolver;
    private final UserService userService;

    public JwtTokenFilter(JwtUtil jwtUtil, HandlerExceptionResolver exceptionResolver, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.exceptionResolver = exceptionResolver;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring("Bearer ".length());
                jwtUtil.validateToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                User user = userService.findByUsername(username);
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(user, token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
