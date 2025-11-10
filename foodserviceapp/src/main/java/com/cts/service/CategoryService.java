package com.cts.service;


import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import com.cts.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO addCategory(CategoryRequestDTO requestCategory);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(int id);
    CategoryResponseDTO getCategoryByName(String name);
    CategoryResponseDTO updateCategory(int id,CategoryRequestDTO requestCategory);
}
