package com.cts.service;

import com.cts.config.JWTUtil;
import com.cts.dto.RegisterCustomerRequestDTO;
import com.cts.dto.RegisterCustomerResponseDTO;
import com.cts.entity.Customer;
import com.cts.entity.User;
import com.cts.exception.UserAlreadyExistsException;
import com.cts.repository.AuthRepository;
import com.cts.repository.CustomerRepository;
import com.cts.repository.DeliveryPartnerRepository;
import com.cts.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CustomerRepository cusRepo;

    @Mock
    private DeliveryPartnerRepository dpRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserInfoConfigManager userInfoConfigManager;

    private AuthServiceImpl authService;

    private RegisterCustomerRequestDTO customerRequestDTO;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authRepository, userRepo, cusRepo, dpRepo,
                modelMapper, jwtUtil, authenticationManager, userInfoConfigManager);

        customerRequestDTO = new RegisterCustomerRequestDTO();
        customerRequestDTO.setName("New Customer");
        customerRequestDTO.setEmail("customer@example.com");
        customerRequestDTO.setPassword("password123");
        customerRequestDTO.setPhno("1234567890");
        customerRequestDTO.setLocation("New York");

        customer = new Customer();
        customer.setName("New Customer");
        customer.setEmail("customer@example.com");
        customer.setPhno("1234567890");
        customer.setLocation("New York");

        user = new User();
        user.setId(1L);
        user.setName("New Customer");
        user.setEmail("customer@example.com");
        user.setRole("CUSTOMER");
    }

    @Test
    @DisplayName("Positive: Create customer successfully")
    void createCustomer_Success() {
        when(userRepo.findByEmail(customerRequestDTO.getEmail())).thenReturn(null);
        when(cusRepo.save(any(Customer.class))).thenReturn(customer);

        RegisterCustomerResponseDTO result = authService.createCustomer(customerRequestDTO);

        assertNotNull(result);
        assertEquals("New Customer", result.getName());
        assertEquals("customer@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Negative: Create customer with existing email throws exception")
    void createCustomer_EmailAlreadyExists() {
        when(userRepo.findByEmail(customerRequestDTO.getEmail())).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.createCustomer(customerRequestDTO);
        });
    }
}
