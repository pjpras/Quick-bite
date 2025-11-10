package com.cts.service;

import com.cts.dto.FoodRequestDTO;
import com.cts.dto.FoodResponseDTO;
import com.cts.entity.Food;

import java.util.List;

public interface FoodService {

    FoodResponseDTO addFoodWithCategoryName(FoodRequestDTO requestFood, String categoryName);
    List<FoodResponseDTO> getAllFood();
    FoodResponseDTO getFoodById(int id);
    List<FoodResponseDTO> getFoodByCategory(String category);
    FoodResponseDTO getFoodByName(String name);
    List<FoodResponseDTO> getFoodByStatus(boolean status);
    List<FoodResponseDTO> getActiveFood();
    FoodResponseDTO updateFood(int id, FoodRequestDTO requestFood);
    FoodResponseDTO updateFoodStatus(int id, boolean status);
    FoodResponseDTO updateFoodPrice(int id, double price);
    FoodResponseDTO updateFoodCategory(int id, String categoryName);
    FoodResponseDTO updateFoodName(int id, String name);
    FoodResponseDTO updateFoodDescription(int id, String description);
    FoodResponseDTO updateFoodImage(int id, String img);


}
