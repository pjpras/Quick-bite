package com.cts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Category name is required")
	@Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
	@Column(nullable = false, unique = true, length = 50)
	private String name;
	
	@NotBlank(message = "Category image is required")
	@Size(max = 500, message = "Image URL must not exceed 500 characters")
	@Column(name = "img", nullable = false, length = 500)
	private String img;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
