package com.cts.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_ID = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String userEmail = request.getHeader(HEADER_USER_EMAIL);
        String userRole = request.getHeader(HEADER_USER_ROLE);
        String userId = request.getHeader(HEADER_USER_ID);

        if (userEmail != null && userRole != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("UserService: Authenticating user from Gateway/Feign headers");
            System.out.println("   Email: " + userEmail);
            System.out.println("   Role: " + userRole);
            System.out.println("   UserId: " + userId);
            System.out.println("   Request URI: " + request.getRequestURI());
            
        
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userEmail,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole))
            );
            
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            System.out.println("UserService: Authentication set successfully for " + request.getRequestURI());
        } else if (userEmail == null || userRole == null) {
            System.out.println("UserService: Missing X-User headers for " + request.getRequestURI());
            System.out.println("   X-User-Email: " + userEmail);
            System.out.println("   X-User-Role: " + userRole);
        }

        filterChain.doFilter(request, response);
    }
}
