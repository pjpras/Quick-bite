package com.cts.service;

import com.cts.config.JWTUtil;
import com.cts.dto.UserResponseDTO;
import com.cts.entity.User;
import com.cts.exception.UserNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private CustomerRepository cusRepo;

    @Mock
    private DeliveryPartnerRepository dpRepo;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserInfoConfigManager userInfoConfigManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(authRepository, modelMapper, jwtUtil,
                authenticationManager, userInfoConfigManager, passwordEncoder);

        try {
            java.lang.reflect.Field userRepoField = UserServiceImpl.class.getDeclaredField("userRepo");
            userRepoField.setAccessible(true);
            userRepoField.set(userService, userRepo);

            java.lang.reflect.Field cusRepoField = UserServiceImpl.class.getDeclaredField("cusRepo");
            cusRepoField.setAccessible(true);
            cusRepoField.set(userService, cusRepo);

            java.lang.reflect.Field dpRepoField = UserServiceImpl.class.getDeclaredField("dpRepo");
            dpRepoField.setAccessible(true);
            dpRepoField.set(userService, dpRepo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setRole("CUSTOMER");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("John Doe");
        userResponseDTO.setEmail("john@example.com");
    }

    @Test
    @DisplayName("Positive: Get user by ID successfully")
    void getUserById_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Negative: Get user by invalid ID throws exception")
    void getUserById_NotFound() {
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }
}
