package com.cts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;


@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header found for path: " + path);
            return chain.filter(exchange);
        }

        try {
           
            String jwt = authHeader.substring(7);
            
            System.out.println("Validating JWT for path: " + path);
  
            if (jwtUtil.validateToken(jwt)) {
              
                String username = jwtUtil.extractUsername(jwt);
                String role = jwtUtil.extractRole(jwt);
                Long userId = jwtUtil.extractUserId(jwt);
                
                System.out.println("JWT Valid - User: " + username + ", Role: " + role + ", UserId: " + userId);               
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                
                

                ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Email", username)
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", role)
                    .build();
                

                	
                
                ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();
                
                return chain.filter(modifiedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
            } else {
                System.err.println("JWT validation failed - invalid token");
            }
        } catch (Exception e) {
            System.err.println("JWT Authentication failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        return chain.filter(exchange);
    }
    
    private boolean isPublicEndpoint(String path) {
        return path.contains("/users/login") || 
               path.contains("/users/signup") ||
               path.contains("/swagger-ui") || 
               path.contains("/v3/api-docs") ||
               path.contains("/eureka");
    }
}
