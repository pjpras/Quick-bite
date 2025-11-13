package com.cts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(auth -> auth

                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .pathMatchers("/app1/api/v1/users/login").permitAll()
                        .pathMatchers("/app1/api/v1/users/signup/**").permitAll()
                        .pathMatchers("/app1/swagger-ui/**", "/app1/v3/api-docs/**").permitAll()
                        .pathMatchers("/app2/swagger-ui/**", "/app2/v3/api-docs/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()

                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/food").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/food/id").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/food/name").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/food/category").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/food/active").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/category").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/category/id").permitAll()
                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/category/categoryName").permitAll()

                        .pathMatchers(HttpMethod.POST, "/app2/api/v1/food/register").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/app2/api/v1/food/update/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/app2/api/v1/food/update/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/app2/api/v1/food/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/app2/api/v1/category/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/app2/api/v1/category/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/app2/api/v1/category/**").hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/customers/active").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/customers/search").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/deliverypartners/active").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/deliverypartners/search").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/customer/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/customer/update")
                        .hasAnyRole("ADMIN", "CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/customer/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/deliverypartner/update")
                        .hasRole("DELIVERY_PARTNER")
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/availability/*").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/totalorder/update").authenticated()

                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/app1/api/v1/users/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/*").authenticated()
                        .pathMatchers(HttpMethod.GET, "/app1/api/v1/users/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/app1/api/v1/users/**").hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/orders/all").authenticated()
                        .pathMatchers(HttpMethod.PATCH, "/app2/api/v1/orders/status/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/app2/api/v1/orders/status/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/app2/api/v1/orders/assign/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/app2/api/v1/orders/assign/**").hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/app2/api/v1/orders/Deliverypartner/**")
                        .hasRole("DELIVERY_PARTNER")
                        .pathMatchers(HttpMethod.PATCH, "/app2/api/v1/orders/Deliverypartner/**")
                        .hasRole("DELIVERY_PARTNER")
                        .pathMatchers(HttpMethod.PUT, "/app2/api/v1/orders/Deliverypartner/**")
                        .hasRole("DELIVERY_PARTNER")

                        .pathMatchers("/app2/api/v1/cart/**").hasRole("CUSTOMER")
                        .pathMatchers("/app2/api/v1/orders/**").hasRole("CUSTOMER")
                        .pathMatchers("/app2/api/v1/feedback/**").hasRole("CUSTOMER")

                        .pathMatchers("/app2/api/v1/delivery/**").hasRole("DELIVERY_PARTNER")

                        .anyExchange().authenticated())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
