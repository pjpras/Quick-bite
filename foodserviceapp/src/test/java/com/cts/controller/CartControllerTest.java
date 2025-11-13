package com.cts.controller;

import com.cts.dto.CartRequestDTO;
import com.cts.dto.CartResponseDTO;
import com.cts.exception.ResourceNotFoundException;
import com.cts.model.User;
import com.cts.service.CartService;
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
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    private User user;
    private CartRequestDTO cartRequestDTO;
    private CartResponseDTO cartResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setFoodId(101);
        cartRequestDTO.setQuantity(2);

        cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setId(1L);
        cartResponseDTO.setFoodId(101);
        cartResponseDTO.setFoodName("Pizza");
        cartResponseDTO.setQuantity(2);
        cartResponseDTO.setPrice(299.99);
    }

    @Test
    @DisplayName("Positive: Add to cart successfully")
    void addToCart_Success() {
        when(authentication.getName()).thenReturn("john@example.com");
        when(userService.getUserByEmail("john@example.com")).thenReturn(user);
        when(cartService.addToCart(1L, cartRequestDTO)).thenReturn(cartResponseDTO);

        ResponseEntity<CartResponseDTO> response = cartController.addToCart(cartRequestDTO, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(101, response.getBody().getFoodId());
        assertEquals("Pizza", response.getBody().getFoodName());
    }

    @Test
    @DisplayName("Negative: Add to cart with invalid user")
    void addToCart_UserNotFound() {
        when(authentication.getName()).thenReturn("invalid@example.com");
        when(userService.getUserByEmail("invalid@example.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartController.addToCart(cartRequestDTO, authentication);
        });
    }
}
