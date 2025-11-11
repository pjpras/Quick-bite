package com.cts.controller;

import java.util.List;

import com.cts.dto.FoodRequestDTO;
import com.cts.dto.FoodResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.service.FoodService;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {


    private FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/register")
    @Operation(summary = "Adding a Food", description = "Adding a Food")
    public ResponseEntity<FoodResponseDTO> addFood(@Valid @RequestBody FoodRequestDTO requestFood) {
        FoodResponseDTO responseFood = foodService.addFoodWithCategoryName(requestFood, requestFood.getCategoryName());
        return new ResponseEntity<>(responseFood, HttpStatus.CREATED);

    }

    @GetMapping
    @Operation(summary = "Getting all Food", description = "Getting all Food")
    public ResponseEntity<List<FoodResponseDTO>> getAllFood() {
        List<FoodResponseDTO> responseFoodlist = foodService.getAllFood();
        return new ResponseEntity<>(responseFoodlist, HttpStatus.OK);

    }

    @GetMapping("/id")
    @Operation(summary = "Getting Food by ID", description = "Getting Food by ID")
    public ResponseEntity<FoodResponseDTO> getFoodById(@RequestParam int id) {
        FoodResponseDTO responsefood = foodService.getFoodById(id);
        if (responsefood != null) {
            return new ResponseEntity<>(responsefood, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/category")
    @Operation(summary = "Getting Food by Category", description = "Getting Food by Category")
    public ResponseEntity<List<FoodResponseDTO>> getFoodByCategory(@RequestParam String category) {
        List<FoodResponseDTO> responseFoodlist = foodService.getFoodByCategory(category);
        return new ResponseEntity<>(responseFoodlist, HttpStatus.OK);

    }

    @GetMapping("/name")
    @Operation(summary = "Getting Food by name", description = "Getting Food by name")
    public ResponseEntity<FoodResponseDTO> getFoodByName(@RequestParam String name) {
        FoodResponseDTO responseFood = foodService.getFoodByName(name);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);

    }
    @GetMapping("/active")
    @Operation(summary = "Getting Active Food with Pagination", description = "Getting active food items with pagination (page size = 8, page starts from 1)")
    public ResponseEntity<Page<FoodResponseDTO>> getActiveFood(@RequestParam(defaultValue = "1") int page) {
        Page<FoodResponseDTO> responseFoodPage = foodService.getActiveFood(page);
        return new ResponseEntity<>(responseFoodPage, HttpStatus.OK);
    }

    @PutMapping("/update/all")
    @Operation(summary = "Updating a Food by id", description = "Updating a Food")
    public ResponseEntity<FoodResponseDTO> updateFoodById(@RequestParam int id, @Valid @RequestBody FoodRequestDTO requestFood) {
        FoodResponseDTO responseFood = foodService.updateFood(id, requestFood);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);

    }

    @PatchMapping("update/status")
    @Operation(summary = "Updating Food Status by id", description = "Updating Food Status by id")
    public ResponseEntity<FoodResponseDTO> updateFoodStatusById(@RequestParam int id, @RequestParam boolean status) {
        FoodResponseDTO responseFood = foodService.updateFoodStatus(id, status);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }
    @PatchMapping("update/price")
    @Operation(summary = "Updating Food Price by id", description = "Updating Food Price by id")
    public ResponseEntity<FoodResponseDTO> updateFoodPriceById(@RequestParam int id, @RequestParam double price) {
        FoodResponseDTO responseFood = foodService.updateFoodPrice(id, price);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }
    @PatchMapping("update/category")
    @Operation(summary = "Updating Food Category by id", description = "Updating Food Category by id")
    public ResponseEntity<FoodResponseDTO> updateFoodCategoryById(@RequestParam int id, @RequestParam String categoryName) {
        FoodResponseDTO responseFood = foodService.updateFoodCategory(id, categoryName);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }

    @PatchMapping("update/name")
    @Operation(summary = "Updating Food Name by id", description = "Updating Food Name by id")
    public ResponseEntity<FoodResponseDTO> updateFoodNameById(@RequestParam int id, @RequestParam String name) {
        FoodResponseDTO responseFood = foodService.updateFoodName(id, name);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }
    @PatchMapping("update/description")
    @Operation(summary = "Updating Food Description by id", description = "Updating Food Description by id")
    public ResponseEntity<FoodResponseDTO> updateFoodDescriptionById(@RequestParam int id, @RequestParam String description) {
        FoodResponseDTO responseFood = foodService.updateFoodDescription(id, description);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }
    @PatchMapping("update/img")
    @Operation(summary = "Updating Food Image by id", description = "Updating Food Image by id")
    public ResponseEntity<FoodResponseDTO> updateFoodImageById(@RequestParam int id, @RequestParam String img) {
        FoodResponseDTO responseFood = foodService.updateFoodImage(id, img);
        return new ResponseEntity<>(responseFood, HttpStatus.OK);
    }

}