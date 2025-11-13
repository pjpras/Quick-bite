package com.cts.controller;

import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import com.cts.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("Italian");
        categoryRequestDTO.setImg("italian.jpg");

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1);
        categoryResponseDTO.setName("Italian");
        categoryResponseDTO.setImg("italian.jpg");
    }

    @Test
    @DisplayName("Positive: Add category successfully")
    void addCategory_Success() {
        when(categoryService.addCategory(categoryRequestDTO)).thenReturn(categoryResponseDTO);

        ResponseEntity<CategoryResponseDTO> response = categoryController.addCategory(categoryRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Italian", response.getBody().getName());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    @DisplayName("Negative: Get category by invalid ID")
    void getCategoryById_NotFound() {
        when(categoryService.getCategoryById(999)).thenReturn(null);

        ResponseEntity<CategoryResponseDTO> response = categoryController.getCategoryById(999);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
