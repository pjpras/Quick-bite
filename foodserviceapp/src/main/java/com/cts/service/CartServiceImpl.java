package com.cts.service;

import com.cts.dto.CartRequestDTO;
import com.cts.dto.CartResponseDTO;
import com.cts.entity.Cart;
import com.cts.entity.Food;

import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.CartRepository;
import com.cts.repository.FoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Override
    @Transactional
    public CartResponseDTO addToCart(Long userId, CartRequestDTO cartRequestDto) {
        System.out.println("CartService - Adding to cart - User ID: " + userId + ", Food ID: " + cartRequestDto.getFoodId());
        
        // Validate user exists via auth service
        com.cts.model.User user = userService.getUserById(userId);
        if (user == null) {
            System.err.println("User not found with id: " + userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        System.out.println("User found: " + user.getName());
        
        Food food = foodRepository.findById(cartRequestDto.getFoodId())
                .orElseThrow(() -> {
                    System.err.println("Food not found with id: " + cartRequestDto.getFoodId());
                    return new ResourceNotFoundException("Food not found with id: " + cartRequestDto.getFoodId());
                });
        
        System.out.println("Food found: " + food.getName());

        if (!food.isStatus()) {
            System.err.println("Food is not active: " + food.getName());
            throw new IllegalStateException("Food item is not available");
        }

        Optional<Cart> existingCart = cartRepository.findByUserIdAndFoodId(userId, cartRequestDto.getFoodId());
        
        Cart cart;
        if (existingCart.isPresent()) {
            System.out.println("Updating existing cart item");

            cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartRequestDto.getQuantity());
        } else {
            System.out.println("Creating new cart item");

            cart = new Cart();
            cart.setUserId(userId);
            cart.setFood(food);
            cart.setQuantity(cartRequestDto.getQuantity());
        }
        
        cart = cartRepository.save(cart);
        System.out.println("Cart saved successfully with ID: " + cart.getId());
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public CartResponseDTO updateQuantity(Long userId, int foodId, int quantity) {
        Cart cart = cartRepository.findByUserIdAndFoodId(userId, foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        cart.setQuantity(quantity);
        cart = cartRepository.save(cart);
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public void removeFromCart(Long userId, int foodId) {
        Cart cart = cartRepository.findByUserIdAndFoodId(userId, foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        cartRepository.delete(cart);
    }
    
    @Override
    public List<CartResponseDTO> getCartItems(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    @Override
    public Map<String, Object> getCartSummary(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        
        double totalAmount = cartItems.stream()
                .mapToDouble(cart -> cart.getFood().getPrice() * cart.getQuantity())
                .sum();
        
        int totalItems = cartItems.stream()
                .mapToInt(Cart::getQuantity)
                .sum();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAmount", totalAmount);
        summary.put("totalItems", totalItems);
        summary.put("itemCount", cartItems.size());
        
        return summary;
    }
    
    private CartResponseDTO convertToDto(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setFoodId(cart.getFood().getId());
        dto.setFoodName(cart.getFood().getName());
        dto.setFoodImage(cart.getFood().getImg());
        dto.setCategoryName(cart.getFood().getCategory() != null ? cart.getFood().getCategory().getName() : null);
        dto.setPrice(cart.getFood().getPrice());
        dto.setQuantity(cart.getQuantity());
        dto.setTotalPrice(cart.getFood().getPrice() * cart.getQuantity());
        dto.setCreatedAt(cart.getCreatedAt());
        return dto;
    }
}
