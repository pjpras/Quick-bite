package com.cts.service;

import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import com.cts.entity.Category;
import com.cts.exception.CategoryAlreadyExistException;
import com.cts.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ModelMapper mapper;

    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepo, mapper);

        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("Italian");
        categoryRequestDTO.setImg("italian.jpg");

        category = new Category();
        category.setId(1);
        category.setName("Italian");
        category.setImg("italian.jpg");

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1);
        categoryResponseDTO.setName("Italian");
        categoryResponseDTO.setImg("italian.jpg");
    }

    @Test
    @DisplayName("Positive: Add category successfully")
    void addCategory_Success() {
        when(categoryRepo.findByName("Italian")).thenReturn(null);
        when(mapper.map(categoryRequestDTO, Category.class)).thenReturn(category);
        when(categoryRepo.save(any(Category.class))).thenReturn(category);
        when(mapper.map(category, CategoryResponseDTO.class)).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.addCategory(categoryRequestDTO);

        assertNotNull(result);
        assertEquals("Italian", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("Negative: Add category with duplicate name")
    void addCategory_AlreadyExists() {
        when(categoryRepo.findByName("Italian")).thenReturn(category);

        assertThrows(CategoryAlreadyExistException.class, () -> {
            categoryService.addCategory(categoryRequestDTO);
        });
    }
}
