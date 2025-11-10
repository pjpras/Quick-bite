package com.cts.controller;

import com.cts.dto.*;
import com.cts.service.AuthService;
import com.cts.service.AuthServiceImpl;
import com.cts.service.UserService;
import com.cts.service.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Users" ,description="User Operations")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;
    private final AuthService authService;

	public UserController(UserService userService, AuthService authService) {
		super();
		this.userService = userService;
		this.authService = authService;
	}


	@Operation(summary = "Register New Customer")
    @PostMapping("/signup/customer")
    public ResponseEntity<RegisterCustomerResponseDTO> createCustomer(@RequestBody RegisterCustomerRequestDTO registerRequestDTO) {
        RegisterCustomerResponseDTO responseDTO = authService.createCustomer(registerRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Register New Delivery Partner")
    @PostMapping("/signup/partner")
    public ResponseEntity<RegisterDeliveryPartnerResponseDTO> createDeliveryPartner(@RequestBody RegisterDeliveryPartnerRequestDTO registerDeliveryPartnerDTO) {
        RegisterDeliveryPartnerResponseDTO registerDeliveryPartnerResponseDTO = authService.createDeliveryPartner(registerDeliveryPartnerDTO);
        return new ResponseEntity<>(registerDeliveryPartnerResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Login User")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginDTO) {
        LoginResponseDTO responseDTO = authService.loginUser(loginDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all the Users")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false) String usertype) {
        List<UserResponseDTO> userResponse = userService.findAllUsers(usertype);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    
    @Operation(summary = "Get Active Customers Only")
    @GetMapping("/customers/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveCustomers() {
        List<UserResponseDTO> customers = userService.getActiveCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @Operation(summary = "Get Active Delivery Partners Only")
    @GetMapping("/deliverypartners/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveDeliveryPartners() {
        List<UserResponseDTO> partners = userService.getActiveDeliveryPartners();
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }
    
    @Operation(summary = "Search Active Customers by Name")
    @GetMapping("/customers/search")
    public ResponseEntity<List<UserResponseDTO>> searchActiveCustomersByName(
        @RequestParam String name
    ) {
        List<UserResponseDTO> customers = userService.searchActiveCustomersByName(name);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @Operation(summary = "Search Active Delivery Partners by Name")
    @GetMapping("/deliverypartners/search")
    public ResponseEntity<List<UserResponseDTO>> searchActiveDeliveryPartnersByName(
        @RequestParam String name
    ) {
        List<UserResponseDTO> partners = userService.searchActiveDeliveryPartnersByName(name);
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    @Operation(summary = "Get user by Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        UserResponseDTO userDTO = userService.getUserById(id);
        if(userDTO!=null)
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        else
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

    }
    
    @Operation(summary = "Get user by Email")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        UserResponseDTO userDTO = userService.getUserByEmail(email);
        if(userDTO!=null) {
        	return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        else {
        	return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete user by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        String deleted = userService.deleteUser(id);
        return new ResponseEntity<String>(deleted,HttpStatus.OK);
    }
    
    @Operation(summary = "Update the Customer")
    @PutMapping("/customer/update")
    public ResponseEntity<RegisterCustomerResponseDTO> updateCustomer(@RequestBody RegisterCustomerRequestDTO registerRequestDTO) {
        RegisterCustomerResponseDTO updatedCustomer = userService.updateCustomer(registerRequestDTO);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }
    
    @Operation(summary = "Update the Delivery Partner")
    @PutMapping("/deliverypartner/update")
    public ResponseEntity<RegisterDeliveryPartnerResponseDTO> updateDeliveryPartner(@RequestBody RegisterDeliveryPartnerRequestDTO dp) {
        RegisterDeliveryPartnerResponseDTO updatedDP = userService.updateDeliveryPartner(dp);
        return new ResponseEntity<>(updatedDP, HttpStatus.OK);
    }
    
    @Operation(summary = "Update Delivery Partner Availability Status")
    @PutMapping("/availability/{id}")
    public ResponseEntity<UserResponseDTO> updateAvailabilityStatus(
        @PathVariable long id,
        @RequestParam Boolean available
    ) {
        UserResponseDTO updatedUser = userService.updateAvailabilityStatus(id, available);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @Operation(summary = "Update User Account Status (Soft Delete)")
    @PatchMapping("/status/{id}")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
        @PathVariable long id,
        @RequestParam boolean status
    ) {
        UserResponseDTO updatedUser = userService.updateUserStatus(id, status);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @Operation(summary = "Update Total Orders of User")
    @PutMapping("/totalorder/update")
    public ResponseEntity<UserResponseDTO> updateTotalOrders(
    		@RequestParam long id
	) {
		UserResponseDTO updatedUser = userService.updateTotalOrders(id);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
}
