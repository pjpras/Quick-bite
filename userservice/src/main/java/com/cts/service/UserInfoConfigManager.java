package com.cts.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.entity.User;
import com.cts.repository.AuthRepository;

@Service
public class UserInfoConfigManager implements UserDetailsService {

    private final AuthRepository authRepository;

    public UserInfoConfigManager(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }


        String dbRole = user.getRole();
        String normalizedRole;

        if (dbRole == null) {
            normalizedRole = "USER";
        } else if (dbRole.equalsIgnoreCase("deliveryPartner")) {
            normalizedRole = "DELIVERY_PARTNER";
        } else if (dbRole.equalsIgnoreCase("customer")) {
            normalizedRole = "CUSTOMER";
        } else {
            normalizedRole = dbRole.toUpperCase();
        }

        String authority = "ROLE_" + normalizedRole;

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authority)
                .build();
    }
}