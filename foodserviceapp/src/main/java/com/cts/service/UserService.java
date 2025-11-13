package com.cts.service;

import com.cts.client.AuthServiceClient;
import com.cts.dto.UserResponseDTO;
import com.cts.model.User;
import com.cts.model.Customer;
import com.cts.model.DeliveryPartner;
import com.cts.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private AuthServiceClient authServiceClient;

    public User getUserById(Long id) {
        try {
            ResponseEntity<UserResponseDTO> response = authServiceClient.getUserById(id);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return convertToUserModel(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }
        return null;
    }

    public User getUserByEmail(String email) {
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            System.out.println("Encoded email: " + encodedEmail);
            ResponseEntity<UserResponseDTO> response = authServiceClient.getUserByEmail(email);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return convertToUserModel(response.getBody());
            }
        } catch (Exception e) {

            System.err.println("REAL FEIGN ERROR: " + e.getMessage());
            throw new RuntimeException("Feign call failed", e);
        }
        return null;
    }

    public List<UserResponseDTO> getActiveCustomers() {
        try {
            ResponseEntity<List<UserResponseDTO>> response = authServiceClient.getActiveCustomers();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error getting active customers: " + e.getMessage());
        }
        return List.of();
    }

    public List<UserResponseDTO> getActiveDeliveryPartners() {
        try {
            ResponseEntity<List<UserResponseDTO>> response = authServiceClient.getActiveDeliveryPartners();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error getting active delivery partners: " + e.getMessage());
        }
        return List.of();
    }

    public List<UserResponseDTO> searchCustomersByName(String name) {
        try {
            ResponseEntity<List<UserResponseDTO>> response = authServiceClient.searchCustomersByName(name);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error searching customers by name: " + e.getMessage());
        }
        return List.of();
    }

    public List<UserResponseDTO> searchDeliveryPartnersByName(String name) {
        try {
            ResponseEntity<List<UserResponseDTO>> response = authServiceClient.searchDeliveryPartnersByName(name);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error searching delivery partners by name: " + e.getMessage());
        }
        return List.of();
    }

    private User convertToUserModel(UserResponseDTO dto) {
        User user;

        if ("customer".equals(dto.getRole())) {
            user = new Customer();
        } else if ("deliveryPartner".equals(dto.getRole())) {
            user = new DeliveryPartner();
        } else if ("admin".equals(dto.getRole())) {
            user = new Admin();
        } else {
            user = new User();
        }

        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhno(dto.getPhno());
        user.setLocation(dto.getLocation());
        user.setRole(dto.getRole());
        user.setAvailabilityStatus(dto.getAvailabilityStatus());
        user.setTotalOrders(dto.getTotalOrders());

        return user;
    }
}