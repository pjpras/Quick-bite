package com.cts.controller;

import com.cts.dto.LoginRequestDTO;
import com.cts.dto.LoginResponseDTO;
import com.cts.service.AuthService;
import com.cts.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    }

    @Test
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
}
