package com.cts.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.cts.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private Integer totalQty;
	@Column( nullable = false)
	private Double totalPrice;
	@Column( nullable = false)
	private LocalDate orderDate;
	private LocalTime orderTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;
	
	@Column(name = "otp", length = 6)
	private String otp;
    
    
	
	private long customer;
    
    
	
	private long deliveryPartner;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems;
	
}
