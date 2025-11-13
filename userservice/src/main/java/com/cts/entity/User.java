package com.cts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="email" , unique = true, nullable = false)
	private String email;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String phno;
	@Column(nullable=false)
	private String location;
	private boolean status=true;
	@Column(name = "availability_status")
	private Boolean availabilityStatus = true; 
	@Column(name = "role", insertable = false, updatable = false)
	private String role;
	@Column(columnDefinition = "int default 0")
    private int totalOrders = 0;
	
}
