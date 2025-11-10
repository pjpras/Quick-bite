package com.cts.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.cts.enums.OrderStatus;

public class OrderResponseDTO {

    private int id;
	private int totalQty;
	private double totalPrice;
	private LocalDate orderDate;
	private LocalTime orderTime;
	private OrderStatus orderStatus;
	private String otp;
    private int customerId;
    private String customerName;
    private int deliveryPartnerId;
    private String assignDeliveryPerson;
    private OrderAddressDTO orderAddress;
    private List<OrderItemResponseDTO> orderItems;


    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public OrderAddressDTO getOrderAddress() {
        return orderAddress;
    }
    public void setOrderAddress(OrderAddressDTO orderAddress) {
        this.orderAddress = orderAddress;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getDeliveryPartnerId() {
        return deliveryPartnerId;
    }
    public void setDeliveryPartnerId(int deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }
    public String getAssignDeliveryPerson() {
        return assignDeliveryPerson;
    }
    public void setAssignDeliveryPerson(String assignDeliveryPerson) {
        this.assignDeliveryPerson = assignDeliveryPerson;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
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
}