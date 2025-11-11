package com.cts.service;

import java.util.List;

import com.cts.dto.CategoryRequestDTO;
import com.cts.dto.CategoryResponseDTO;
import com.cts.exception.CategoryAlreadyExistException;
import com.cts.exception.CategoryNotFoundException;
import com.cts.repository.CategoryRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cts.entity.Category;


@Service
public class CategoryServiceImpl implements CategoryService{


    private CategoryRepository categoryRepo;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepo, ModelMapper mapper) {
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    public CategoryResponseDTO addCategory(CategoryRequestDTO requestCategory) {
        if(categoryRepo.findByName(requestCategory.getName())!=null){
            throw new CategoryAlreadyExistException("Category already exists with name - "+requestCategory.getName());
        }
        Category category= mapper.map(requestCategory, Category.class);
        category=categoryRepo.save(category);
        CategoryResponseDTO responseCategory= mapper.map(category, CategoryResponseDTO.class);
        return responseCategory;
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> category = categoryRepo.findAll();
        if(category.size()==0){
            throw new CategoryNotFoundException("No categories found");
        }
        List<CategoryResponseDTO> responseCategorieslist = category.stream()
                .map(cat -> mapper.map(cat, CategoryResponseDTO.class))
                .toList();
        return responseCategorieslist;
    }

    public CategoryResponseDTO getCategoryById(int id) {
        Category category=categoryRepo.findById(id).orElseThrow(()->new CategoryNotFoundException("Category not found with id - "+id));
        CategoryResponseDTO responseCategory= mapper.map(category, CategoryResponseDTO.class);
        return responseCategory;

    }

    public CategoryResponseDTO getCategoryByName(String name) {
        Category category=categoryRepo.findByName(name);
        if(category == null){
            throw new CategoryNotFoundException("Category not found with name - "+name);
        }
        CategoryResponseDTO responseCategory= mapper.map(category, CategoryResponseDTO.class);
        return responseCategory;

    }

    public CategoryResponseDTO updateCategory(int id,CategoryRequestDTO requestCategory) {
        Category existingCategory=categoryRepo.findById(id).orElseThrow(()->new CategoryNotFoundException("Category not found with id - "+id));
        existingCategory.setName(requestCategory.getName());
        existingCategory.setImg(requestCategory.getImg());
        existingCategory=categoryRepo.save(existingCategory);
        CategoryResponseDTO responseCategory= mapper.map(existingCategory, CategoryResponseDTO.class);
        return responseCategory;
    }

    @Override
    public void deleteCategory(int id) {
        Category category = categoryRepo.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id - " + id));
        categoryRepo.delete(category);
    }
}