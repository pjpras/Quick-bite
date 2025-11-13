package com.cts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column( nullable = false, length = 15)
	private String firstName;
    @Column( nullable = false, length = 15)
	private String lastName;
	 @Column( nullable = false, length = 20)
	private String street;
	 @Column( nullable = false, length = 20)
	private String city;
	 @Column( nullable = false, length = 20)
	private String state;
	 @Column( nullable = false, length = 20)
	private String pin;
	 @Column( nullable = false, length = 20)
	private String phoneNo;
	@OneToOne
	@JoinColumn(name="order_id")
	private Order order;
	
}
