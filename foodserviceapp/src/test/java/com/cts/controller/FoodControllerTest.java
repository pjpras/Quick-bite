package com.cts.controller;

import com.cts.dto.FoodRequestDTO;
import com.cts.dto.FoodResponseDTO;
import com.cts.service.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodControllerTest {

    @Mock
    private FoodService foodService;

    private FoodController foodController;

    private FoodRequestDTO foodRequestDTO;
    private FoodResponseDTO foodResponseDTO;

    @BeforeEach
    void setUp() {
        foodController = new FoodController(foodService);

        foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Burger");
        foodRequestDTO.setPrice(199.99);
        foodRequestDTO.setCategoryName("Fast Food");

        foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setId(1);
        foodResponseDTO.setName("Burger");
        foodResponseDTO.setPrice(199.99);
    }

    @Test
    @DisplayName("Positive: Add food successfully")
    void addFood_Success() {
        when(foodService.addFoodWithCategoryName(any(FoodRequestDTO.class), any(String.class)))
                .thenReturn(foodResponseDTO);

        ResponseEntity<FoodResponseDTO> response = foodController.addFood(foodRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Burger", response.getBody().getName());
    }

    @Test
    @DisplayName("Negative: Get food by ID not found")
    void getFoodById_NotFound() {
        when(foodService.getFoodById(999)).thenReturn(null);

        ResponseEntity<FoodResponseDTO> response = foodController.getFoodById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
