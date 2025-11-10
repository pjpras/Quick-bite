package com.cts.service;
import com.cts.config.JWTUtil;
import com.cts.dto.*;
import com.cts.entity.Customer;
import com.cts.entity.DeliveryPartner;
import com.cts.exception.InvalidCredentialsException;
import com.cts.exception.UserAlreadyExistsException;
import com.cts.repository.CustomerRepository;
import com.cts.repository.DeliveryPartnerRepository;
import com.cts.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.entity.User;
import com.cts.repository.AuthRepository;




@Service
public class AuthServiceImpl implements AuthService {
	
    private AuthRepository authRepository;
   
    private UserRepository userRepo;
    private CustomerRepository cusRepo;
    private DeliveryPartnerRepository dpRepo;
    private ModelMapper modelMapper;
    private JWTUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserInfoConfigManager userInfoConfigManager;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AuthServiceImpl(AuthRepository authRepository, UserRepository userRepo, CustomerRepository cusRepo,
			DeliveryPartnerRepository dpRepo, ModelMapper modelMapper, JWTUtil jwtUtil,
			AuthenticationManager authenticationManager, UserInfoConfigManager userInfoConfigManager) {
		super();
		this.authRepository = authRepository;
		this.userRepo = userRepo;
		this.cusRepo = cusRepo;
		this.dpRepo = dpRepo;
		this.modelMapper = modelMapper;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.userInfoConfigManager = userInfoConfigManager;
	}



	public RegisterCustomerResponseDTO createCustomer(RegisterCustomerRequestDTO registerRequestDTO) {
        if (userRepo.findByEmail(registerRequestDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Customer newCustomer = new Customer();
        newCustomer.setEmail(registerRequestDTO.getEmail());
        newCustomer.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword())); // hash password
        newCustomer.setName(registerRequestDTO.getName());
        newCustomer.setPhno(registerRequestDTO.getPhno());
        newCustomer.setLocation(registerRequestDTO.getLocation());
        newCustomer.setStatus(true);

        Customer savedCustomer = cusRepo.save(newCustomer);

        RegisterCustomerResponseDTO responseDTO = new RegisterCustomerResponseDTO();
        responseDTO.setEmail(savedCustomer.getEmail());
        responseDTO.setName(savedCustomer.getName());
        responseDTO.setPhno(savedCustomer.getPhno());
        responseDTO.setLocation(savedCustomer.getLocation());
        return responseDTO;
    }

    public RegisterDeliveryPartnerResponseDTO createDeliveryPartner(RegisterDeliveryPartnerRequestDTO registerDeliveryPartnerDTO) {
        if (userRepo.findByEmail(registerDeliveryPartnerDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        DeliveryPartner newDeliveryPartner = new DeliveryPartner();
        newDeliveryPartner.setEmail(registerDeliveryPartnerDTO.getEmail());
        newDeliveryPartner.setPassword(passwordEncoder.encode(registerDeliveryPartnerDTO.getPassword())); // hash password
        newDeliveryPartner.setName(registerDeliveryPartnerDTO.getName());
        newDeliveryPartner.setPhno(registerDeliveryPartnerDTO.getPhno());
        newDeliveryPartner.setLocation(registerDeliveryPartnerDTO.getLocation());
        newDeliveryPartner.setStatus(true);

        DeliveryPartner savedDeliveryPartner = dpRepo.save(newDeliveryPartner);

        RegisterDeliveryPartnerResponseDTO responseDTO = new RegisterDeliveryPartnerResponseDTO();
        responseDTO.setEmail(savedDeliveryPartner.getEmail());
        responseDTO.setName(savedDeliveryPartner.getName());
        responseDTO.setPhno(savedDeliveryPartner.getPhno());
        responseDTO.setLocation(savedDeliveryPartner.getLocation());
        return responseDTO;
    }


    public LoginResponseDTO loginUser(LoginRequestDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User existingUser = userRepo.findByEmail(loginDTO.getEmail());
        if (existingUser == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String role = existingUser.getRole();
        String normalizedRole = role
            .replaceAll("([a-z])([A-Z])", "$1_$2")  // camelCase to snake_case
            .toUpperCase();  

        String token = jwtUtil.generateToken(
            existingUser.getEmail(), 
            existingUser.getId(), 
            normalizedRole
        );

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setId(existingUser.getId());
        responseDTO.setEmail(existingUser.getEmail());
        responseDTO.setName(existingUser.getName());
        responseDTO.setRole(existingUser.getRole());
        responseDTO.setAccessToken(token);
        return responseDTO;
    }

}