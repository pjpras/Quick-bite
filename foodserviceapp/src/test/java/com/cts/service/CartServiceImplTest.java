package com.cts.service;

import com.cts.dto.CartRequestDTO;
import com.cts.dto.CartResponseDTO;
import com.cts.entity.Cart;
import com.cts.entity.Food;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.CartRepository;
import com.cts.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Food food;
    private Cart cart;
    private CartRequestDTO cartRequestDTO;

    @BeforeEach
    void setUp() {
        food = new Food();
        food.setId(101);
        food.setName("Pizza");
        food.setPrice(299.99);

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setFood(food);
        cart.setQuantity(2);

        cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setFoodId(101);
        cartRequestDTO.setQuantity(2);
    }

    @Test
    @DisplayName("Positive: Add to cart successfully")
    void addToCart_Success() {
        com.cts.model.User user = new com.cts.model.User();
        user.setId(1L);
        user.setName("Test User");

        food.setStatus(true); // Set food as active

        when(userService.getUserById(1L)).thenReturn(user);
        when(foodRepository.findById(101)).thenReturn(Optional.of(food));
        when(cartRepository.findByUserIdAndFoodId(1L, 101)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDTO result = cartService.addToCart(1L, cartRequestDTO);

        assertNotNull(result);
        assertEquals(101, result.getFoodId());
        assertEquals("Pizza", result.getFoodName());
    }

    @Test
    @DisplayName("Negative: Add to cart with invalid food ID")
    void addToCart_FoodNotFound() {
        com.cts.model.User user = new com.cts.model.User();
        user.setId(1L);
        user.setName("Test User");

        CartRequestDTO invalidRequest = new CartRequestDTO();
        invalidRequest.setFoodId(999);
        invalidRequest.setQuantity(2);

        when(userService.getUserById(1L)).thenReturn(user);
        when(foodRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addToCart(1L, invalidRequest);
        });
    }
}
