package com.cts.controller;

import java.util.List;

import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.entity.Category;
import com.cts.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {


    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/register")
    @Operation( summary = "Adding a Category", description = "Adding a Category" )
    public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @RequestBody CategoryRequestDTO requestCategory) {
        CategoryResponseDTO responseCategory = categoryService.addCategory(requestCategory);
        return new ResponseEntity<>(responseCategory, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation( summary = "Getting all Category", description = "Getting all Category" )
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> responseCategorieslist = categoryService.getAllCategories();
        return new ResponseEntity<>(responseCategorieslist, HttpStatus.OK);
    }

    @GetMapping("/id")
    @Operation( summary = "Getting Category by ID", description = "Getting Category by ID" )
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@RequestParam int id) {
        CategoryResponseDTO responseCategory = categoryService.getCategoryById(id);
        if (responseCategory != null) {
            return new ResponseEntity<>(responseCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/categoryname")
    @Operation( summary = "Getting Category by Name", description = "Getting Category by Name" )
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(@RequestParam  String name) {
        CategoryResponseDTO responseCategory = categoryService.getCategoryByName(name);
        if (responseCategory != null) {
            return new ResponseEntity<>(responseCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/update")
    @Operation( summary = "Updating a Category by id", description = "Updating a Category" )
    public ResponseEntity<CategoryResponseDTO> updateCategoryById(@Valid @RequestParam int id , @RequestBody CategoryRequestDTO requestCategory) {
        CategoryResponseDTO responseCategory = categoryService.updateCategory(id,requestCategory);
        if (responseCategory != null) {
            return new ResponseEntity<>(responseCategory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}