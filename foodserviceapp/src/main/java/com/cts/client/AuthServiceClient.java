package com.cts.client;

import com.cts.dto.UserResponseDTO;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "USERSERVICEAPP")
public interface AuthServiceClient {
    
    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/api/v1/users/email/{email}")
    ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable("email") String email);
    
    @GetMapping("/api/v1/users")
    ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(value = "usertype", required = false) String usertype);
    
    @GetMapping("/api/v1/users/customers/active")
    ResponseEntity<List<UserResponseDTO>> getActiveCustomers();
    
    @GetMapping("/api/v1/users/deliverypartners/active")
    ResponseEntity<List<UserResponseDTO>> getActiveDeliveryPartners();
    
    @GetMapping("/api/v1/users/customers/search")
    ResponseEntity<List<UserResponseDTO>> searchCustomersByName(@RequestParam("name") String name);
    
    @GetMapping("/api/v1/users/deliverypartners/search")
    ResponseEntity<List<UserResponseDTO>> searchDeliveryPartnersByName(@RequestParam("name") String name);
    
    @PutMapping("/api/v1/users/availability/{id}")
    ResponseEntity<UserResponseDTO> updateDeliveryPartnerAvailability(@PathVariable("id") Long id, @RequestParam("available") Boolean available);
    
    @PutMapping("/api/v1/users/totalorder/update")
    ResponseEntity<UserResponseDTO> updateTotalOrders(@RequestParam("id") Long id);
}