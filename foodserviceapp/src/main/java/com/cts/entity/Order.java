package com.cts.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.cts.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name="orders")
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
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public LocalTime getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(LocalTime orderTime) {
		this.orderTime = orderTime;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	public long getCustomer() {
		return customer;
	}
	public void setCustomer(long customer) {
		this.customer = customer;
	}
	public long getDeliveryPartner() {
		return deliveryPartner;
	}
	public void setDeliveryPartner(long deliveryPartner) {
		this.deliveryPartner = deliveryPartner;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	

}
