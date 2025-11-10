package com.cts.controller;

import com.cts.dto.CartRequestDTO;
import com.cts.dto.CartResponseDTO;
import com.cts.exception.ResourceNotFoundException;
import com.cts.service.CartService;
import com.cts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @Valid @RequestBody CartRequestDTO cartRequestDto,
            Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        CartResponseDTO response = cartService.addToCart(user.getId(), cartRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PatchMapping("/update/{foodId}")
    public ResponseEntity<CartResponseDTO> updateQuantity(
            @PathVariable int foodId,
            @RequestParam int quantity,
            Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        CartResponseDTO response = cartService.updateQuantity(user.getId(), foodId, quantity);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/remove/{foodId}")
    public ResponseEntity<String> removeFromCart(
            @PathVariable int foodId,
            Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        cartService.removeFromCart(user.getId(), foodId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }
    
    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getCartItems(Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        List<CartResponseDTO> cartItems = cartService.getCartItems(user.getId());
        return ResponseEntity.ok(cartItems);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        cartService.clearCart(user.getId());
        return ResponseEntity.ok("Cart cleared successfully");
    }
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getCartSummary(Authentication authentication) {
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        Map<String, Object> summary = cartService.getCartSummary(user.getId());
        return ResponseEntity.ok(summary);
    }
}
