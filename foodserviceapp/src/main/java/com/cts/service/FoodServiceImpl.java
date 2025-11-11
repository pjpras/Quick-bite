package com.cts.service;

import java.util.ArrayList;
import java.util.List;

import com.cts.dto.FoodRequestDTO;
import com.cts.dto.FoodResponseDTO;
import com.cts.exception.CategoryNotFoundException;
import com.cts.exception.FoodAlreadyExistsException;
import com.cts.exception.FoodNotFoundException;
import com.cts.exception.PriceNotPositiveException;
import com.cts.repository.CategoryRepository;
import com.cts.repository.FoodRepository;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cts.entity.Category;
import com.cts.entity.Food;


@Service
public class FoodServiceImpl implements FoodService {

	private static final int PAGE_SIZE = 8; // Hardcoded page size

	private FoodRepository repo;
	private CategoryRepository categoryRepo;
	private ModelMapper mapper;

	public FoodServiceImpl(FoodRepository repo, CategoryRepository categoryRepo, ModelMapper mapper) {
		this.repo = repo;
		this.categoryRepo = categoryRepo;
		this.mapper = mapper;
	}

	public FoodResponseDTO addFoodWithCategoryName(FoodRequestDTO requestFood, String categoryName) {
		if (repo.findByName(requestFood.getName()) != null) {
			throw new FoodAlreadyExistsException("Food already exists with name - " + requestFood.getName());
		}

		if (categoryName == null || categoryName.trim().isEmpty()) {
			throw new CategoryNotFoundException("Category name is required to add food");
		}

		Category category = categoryRepo.findByName(categoryName.trim());
		if (category == null) {
			throw new CategoryNotFoundException("Category not found with name - " + categoryName + ". Please create the category first.");
		}

		Food food = mapper.map(requestFood, Food.class);
		food.setCategory(category);

		food = repo.save(food);
		return mapper.map(food, FoodResponseDTO.class);
	}


	public List<FoodResponseDTO> getAllFood() {
		List<Food> list = repo.findAll();
		if (list.isEmpty()) {
			throw new FoodNotFoundException("No Food Found");
		}
		List<FoodResponseDTO> responseFoodlist = new ArrayList<>();

		for (Food food : list) {
			FoodResponseDTO res = mapper.map(food, FoodResponseDTO.class);
			responseFoodlist.add(res);
		}
		return responseFoodlist;
	}


	public FoodResponseDTO getFoodById(int id) {
		Food food = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		return mapper.map(food, FoodResponseDTO.class);
	}

	public List<FoodResponseDTO> getFoodByCategory(String category) {
		List<Food> food = repo.findByCategoryName(category);
		if (food.isEmpty()) {
			throw new FoodNotFoundException("No Food Found with Category - " + category);
		}
		List<FoodResponseDTO> responseFoodlist = new ArrayList<>();
		for (Food f : food) {
			FoodResponseDTO res = mapper.map(f, FoodResponseDTO.class);
			responseFoodlist.add(res);
		}
		return responseFoodlist;
	}

	public FoodResponseDTO getFoodByName(String name){
		Food food = repo.findByName(name);
		if (food == null) {
			throw new FoodNotFoundException("Food not found with name - " + name);
		}
		return mapper.map(food, FoodResponseDTO.class);
	}

	public List<FoodResponseDTO> getFoodByStatus(boolean status) {
		List<Food> food = repo.findByStatus(status);
		if (food.isEmpty()) {
			throw new FoodNotFoundException("No Food Found with status - " + status);
		}
		List<FoodResponseDTO> responseFoodlist = new ArrayList<>();
		for (Food f : food) {
			FoodResponseDTO res = mapper.map(f, FoodResponseDTO.class);
			responseFoodlist.add(res);
		}
		return responseFoodlist;
	}
	
	public Page<FoodResponseDTO> getActiveFood(int page) {
		
		int zeroBasedPage = page - 1;
		if (zeroBasedPage < 0) {
			zeroBasedPage = 0; 
		}
		
		Pageable pageable = PageRequest.of(zeroBasedPage, PAGE_SIZE);
		Page<Food> foodPage = repo.findByStatus(true, pageable);
		
		if (foodPage.isEmpty()) {
			throw new FoodNotFoundException("No Active Food Found");
		}
		
		return foodPage.map(food -> mapper.map(food, FoodResponseDTO.class));
	}

	public FoodResponseDTO updateFood(int id, FoodRequestDTO requestFood) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));

		existingFood.setName(requestFood.getName());
		existingFood.setImg(requestFood.getImg());
		existingFood.setPrice(requestFood.getPrice());
		existingFood.setDescription(requestFood.getDescription());
		existingFood.setStatus(requestFood.isStatus());

		if (requestFood.getCategoryName() != null && !requestFood.getCategoryName().trim().isEmpty()) {
			Category category = categoryRepo.findByName(requestFood.getCategoryName().trim());
			if (category == null) {
				throw new CategoryNotFoundException("Category not found with name - " + requestFood.getCategoryName() + ". Please create the category first.");
			}
			existingFood.setCategory(category);
		}

		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}
	public FoodResponseDTO updateFoodStatus(int id, boolean status) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		existingFood.setStatus(status);
		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}
	public FoodResponseDTO updateFoodPrice(int id, double price) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		if(price<=0){
			throw new PriceNotPositiveException("Price must be greater than zero");
		}
		existingFood.setPrice(price);
		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}

	public FoodResponseDTO updateFoodCategory(int id, String categoryName) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));

		if (categoryName == null || categoryName.trim().isEmpty()) {
			throw new CategoryNotFoundException("Category name is required");
		}
		
		Category category = categoryRepo.findByName(categoryName.trim());
		if (category == null) {
			throw new CategoryNotFoundException("Category not found with name - " + categoryName + ". Please create the category first.");
		}
		existingFood.setCategory(category);

		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}

	public FoodResponseDTO updateFoodName(int id, String name) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		existingFood.setName(name);
		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}
	public FoodResponseDTO updateFoodDescription(int id, String description) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		existingFood.setDescription(description);
		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}

	public FoodResponseDTO updateFoodImage(int id, String img) {
		Food existingFood = repo.findById(id).orElseThrow(() -> new FoodNotFoundException("Food not found with id - " + id));
		existingFood.setImg(img);
		existingFood = repo.save(existingFood);
		return mapper.map(existingFood, FoodResponseDTO.class);
	}



}
