package com.cts.service;

import com.cts.dto.FoodRequestDTO;
import com.cts.dto.FoodResponseDTO;
import com.cts.entity.Category;
import com.cts.entity.Food;
import com.cts.exception.FoodNotFoundException;
import com.cts.repository.CategoryRepository;
import com.cts.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper mapper;

    private FoodServiceImpl foodService;

    private Food food;
    private Category category;
    private FoodRequestDTO foodRequestDTO;
    private FoodResponseDTO foodResponseDTO;

    @BeforeEach
    void setUp() {
        foodService = new FoodServiceImpl(foodRepository, categoryRepository, mapper);

        category = new Category();
        category.setId(1);
        category.setName("Fast Food");

        food = new Food();
        food.setId(1);
        food.setName("Burger");
        food.setPrice(199.99);
        food.setCategory(category);

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
    @DisplayName("Positive: Add food with category name successfully")
    void addFoodWithCategoryName_Success() {
        when(foodRepository.findByName("Burger")).thenReturn(null);
        when(categoryRepository.findByName("Fast Food")).thenReturn(category);
        when(mapper.map(any(FoodRequestDTO.class), any())).thenReturn(food);
        when(foodRepository.save(any(Food.class))).thenReturn(food);
        when(mapper.map(any(Food.class), any())).thenReturn(foodResponseDTO);

        FoodResponseDTO result = foodService.addFoodWithCategoryName(foodRequestDTO, "Fast Food");

        assertNotNull(result);
        assertEquals("Burger", result.getName());
    }

    @Test
    @DisplayName("Negative: Get food by ID not found")
    void getFoodById_NotFound() {
        when(foodRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(FoodNotFoundException.class, () -> {
            foodService.getFoodById(999);
        });
    }
}
