package com.cts.service;

import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import java.util.List;

public interface CategoryService {
    CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequest);
    CategoryResponseDTO getCategoryById(int id);
    CategoryResponseDTO getCategoryByName(String name);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO updateCategory(int id, CategoryRequestDTO categoryRequest);
    void deleteCategory(int id);
}
