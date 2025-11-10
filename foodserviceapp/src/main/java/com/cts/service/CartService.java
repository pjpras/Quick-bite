package com.cts.service;

import com.cts.dto.CartRequestDTO;
import com.cts.dto.CartResponseDTO;

import java.util.List;
import java.util.Map;

public interface CartService {
    
    CartResponseDTO addToCart(Long userId, CartRequestDTO cartRequestDto);
    CartResponseDTO updateQuantity(Long userId, int foodId, int quantity);
    void removeFromCart(Long userId, int foodId);
    List<CartResponseDTO> getCartItems(Long userId);
    void clearCart(Long userId);
    Map<String, Object> getCartSummary(Long userId);
}
