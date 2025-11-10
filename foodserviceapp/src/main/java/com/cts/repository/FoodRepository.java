package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Integer>{

	@Query("SELECT f FROM Food f WHERE f.category.name = :categoryName")
	List<Food> findByCategoryName(@Param("categoryName") String categoryName);
	List<Food> findByStatus(Boolean status);
	Food findByName(String name);

	
}
