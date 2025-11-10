package com.cts.service;

import java.util.Arrays;
import java.util.List;

import com.cts.config.JWTUtil;
import com.cts.dto.*;
import com.cts.entity.Customer;
import com.cts.entity.DeliveryPartner;
import com.cts.repository.AuthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.entity.User;
import com.cts.exception.UserNotFoundException;
import com.cts.repository.CustomerRepository;
import com.cts.repository.DeliveryPartnerRepository;
import com.cts.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired private UserRepository userRepo;
	@Autowired private CustomerRepository cusRepo;
	@Autowired private DeliveryPartnerRepository dpRepo;

	private final AuthRepository authRepository;
	private final ModelMapper modelMapper;
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final UserInfoConfigManager userInfoConfigManager;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(AuthRepository authRepository, ModelMapper modelMapper, JWTUtil jwtUtil,
						   AuthenticationManager authenticationManager,
						   UserInfoConfigManager userInfoConfigManager,
						   PasswordEncoder passwordEncoder) {
		this.authRepository = authRepository;
		this.modelMapper = modelMapper;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.userInfoConfigManager = userInfoConfigManager;
		this.passwordEncoder = passwordEncoder;
	}


	public List<UserResponseDTO> findAllUsers(String usertype) {
		if (usertype.equalsIgnoreCase("customer")) {
			List<User> customers = userRepo.findByRoleAndStatusTrue("customer");
			return Arrays.asList(modelMapper.map(customers, UserResponseDTO[].class));
		} else {
			List<User> deliveryPartners = userRepo.findByRoleAndStatusTrue("deliveryPartner");
			return Arrays.asList(modelMapper.map(deliveryPartners, UserResponseDTO[].class));
		}
	}

	public List<User> findAllDeliveryPartners() {
		return userRepo.findAllDeliveryPartners();
	}

	public UserResponseDTO getUserById(long id) {
		User existingUser = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User not found with id "+ id));
		return modelMapper.map(existingUser, UserResponseDTO.class);
	}

	public UserResponseDTO getUserByEmail(String email) {
		User existingUser = userRepo.findByEmail(email);
		if (existingUser == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		return modelMapper.map(existingUser, UserResponseDTO.class);
	}

	public String deleteUser(long id) {
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			user.setStatus(false);
			userRepo.save(user);
			return "Succesfully deleted user with " + id;
		} else {
			return " User Not Found";
		}
	}


	public RegisterCustomerResponseDTO updateCustomer(RegisterCustomerRequestDTO registerRequestDTO) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        
		User existingUser = userRepo.findByEmail(email);
		
		if(!(existingUser instanceof Customer)) throw new UserNotFoundException("User with email: " + email + " is not a customer");

		else {
			if (existingUser == null) throw new UserNotFoundException("Customer not found with email: " + email);
			
			
			if (registerRequestDTO.getName() != null && !registerRequestDTO.getName().isEmpty()) {
				existingUser.setName(registerRequestDTO.getName());
			}
			if (registerRequestDTO.getEmail() != null && !registerRequestDTO.getEmail().isEmpty()) {
				existingUser.setEmail(registerRequestDTO.getEmail());
			}
			if (registerRequestDTO.getPassword() != null && !registerRequestDTO.getPassword().isEmpty()) {
				existingUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
			}
			if(registerRequestDTO.getLocation() != null && !registerRequestDTO.getLocation().isEmpty()) {
				((Customer) existingUser).setLocation(registerRequestDTO.getLocation());
			}
			if(registerRequestDTO.getPhno() != null && !registerRequestDTO.getPhno().isEmpty()) {
				((Customer) existingUser).setPhno(registerRequestDTO.getPhno());
			}
			Customer updatedCustomer = cusRepo.save((Customer) existingUser);
			
			return modelMapper.map(updatedCustomer, RegisterCustomerResponseDTO.class);
		}
		
	}



	public RegisterDeliveryPartnerResponseDTO updateDeliveryPartner(RegisterDeliveryPartnerRequestDTO registerDeliveryPartnerDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
		
		User existingUser = userRepo.findByEmail(email); 
		if (existingUser == null) {
			throw new UserNotFoundException("Delivery Partner not found with email: " + email);
		}
		if(!(existingUser instanceof DeliveryPartner)) throw new UserNotFoundException("User with email: " + email + " is not a Delivery Partner");
		
		if (registerDeliveryPartnerDTO.getName() != null && !registerDeliveryPartnerDTO.getName().isEmpty()) {
			existingUser.setName(registerDeliveryPartnerDTO.getName());
		}
		if (registerDeliveryPartnerDTO.getEmail() != null && !registerDeliveryPartnerDTO.getEmail().isEmpty()) {
			existingUser.setEmail(registerDeliveryPartnerDTO.getEmail());
		}
		if (registerDeliveryPartnerDTO.getPassword() != null && !registerDeliveryPartnerDTO.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(registerDeliveryPartnerDTO.getPassword()));
		}
		if(registerDeliveryPartnerDTO.getLocation() != null && !registerDeliveryPartnerDTO.getLocation().isEmpty()) {
			((DeliveryPartner) existingUser).setLocation(registerDeliveryPartnerDTO.getLocation());
		}
		if(registerDeliveryPartnerDTO.getPhno() != null && !registerDeliveryPartnerDTO.getPhno().isEmpty()) {
			((DeliveryPartner) existingUser).setPhno(registerDeliveryPartnerDTO.getPhno());
		}
		DeliveryPartner updatedDeliveryPartner = dpRepo.save((DeliveryPartner) existingUser);
		return modelMapper.map(updatedDeliveryPartner, RegisterDeliveryPartnerResponseDTO.class);
	}
	
	public UserResponseDTO updateAvailabilityStatus(long id, Boolean available) {
		User user = userRepo.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	
		if (!(user instanceof DeliveryPartner)) {
			throw new IllegalArgumentException("Only delivery partners can have availability status");
		}
		
		user.setAvailabilityStatus(available);
		User updatedUser = userRepo.save(user);
		return modelMapper.map(updatedUser, UserResponseDTO.class);
	}
	
	public List<UserResponseDTO> getActiveCustomers() {
		List<User> customers = userRepo.findByRoleAndStatus("customer", true);
		return Arrays.asList(modelMapper.map(customers, UserResponseDTO[].class));
	}
	
	public List<UserResponseDTO> getActiveDeliveryPartners() {
		List<User> partners = userRepo.findByRoleAndStatus("deliveryPartner", true);
		return Arrays.asList(modelMapper.map(partners, UserResponseDTO[].class));
	}
	
	public List<UserResponseDTO> searchActiveCustomersByName(String name) {
		List<User> customers = userRepo.findByRoleAndStatusTrueAndNameContainingIgnoreCase("customer", name);
		return Arrays.asList(modelMapper.map(customers, UserResponseDTO[].class));
	}
	
	public List<UserResponseDTO> searchActiveDeliveryPartnersByName(String name) {

		List<User> partners = userRepo.findByRoleAndStatusTrueAndNameContainingIgnoreCase("deliveryPartner", name);
		return Arrays.asList(modelMapper.map(partners, UserResponseDTO[].class));
	}
	
	public UserResponseDTO updateUserStatus(long id, boolean status) {
		User user = userRepo.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		user.setStatus(status);
		User updatedUser = userRepo.save(user);
		return modelMapper.map(updatedUser, UserResponseDTO.class);
	}
	
	public UserResponseDTO updateTotalOrders(long id) {
		User user = userRepo.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		user.setTotalOrders(user.getTotalOrders() + 1);
		User updatedUser = userRepo.save(user);
		return modelMapper.map(updatedUser, UserResponseDTO.class);
	}
}






