package com.cts.controller;

import com.cts.dto.*;
import com.cts.service.AuthService;
import com.cts.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password123");

        loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(1L);
        loginResponseDTO.setEmail("test@example.com");
        loginResponseDTO.setName("Test User");
        loginResponseDTO.setRole("CUSTOMER");
        loginResponseDTO.setAccessToken("dummy.jwt.token");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("John Doe");
        userResponseDTO.setEmail("john@example.com");
    }

    @Test
    @DisplayName("Positive: Login user successfully")
    void loginUser_Success() {
        when(authService.loginUser(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        ResponseEntity<LoginResponseDTO> response = userController.loginUser(loginRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(loginResponseDTO.getEmail(), responseBody.getEmail());
        assertEquals(loginResponseDTO.getAccessToken(), responseBody.getAccessToken());
        assertEquals(loginResponseDTO.getId(), responseBody.getId());
        assertEquals(loginResponseDTO.getName(), responseBody.getName());
        assertEquals(loginResponseDTO.getRole(), responseBody.getRole());
    }

    @Test
    @DisplayName("Positive: Get user by ID successfully")
    void getUserById_Success() {
        when(userService.getUserById(1L)).thenReturn(userResponseDTO);

        ResponseEntity<?> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserResponseDTO);
        UserResponseDTO responseBody = (UserResponseDTO) response.getBody();
        assertEquals(1L, responseBody.getId());
        assertEquals("John Doe", responseBody.getName());
        assertEquals("john@example.com", responseBody.getEmail());
    }

    @Test
    @DisplayName("Negative: Get user by invalid ID - Not Found")
    void getUserById_NotFound() {
        when(userService.getUserById(999L)).thenReturn(null);

        ResponseEntity<?> response = userController.getUserById(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    @DisplayName("Negative: Get user by invalid email - Not Found")
    void getUserByEmail_NotFound() {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(null);

        ResponseEntity<?> response = userController.getUserByEmail("notfound@example.com");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }
}
