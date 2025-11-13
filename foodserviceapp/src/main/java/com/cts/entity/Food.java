package com.cts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	 @Column(nullable = false, length = 20)
	private String name;

	private String img;
	@Column(nullable = false)
	private double price;
	@Column(nullable = false, length = 500)
	private String description;
	
	private boolean status;
	private float avgRating;
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@Transient
	private String categoryName;
	
	
}
