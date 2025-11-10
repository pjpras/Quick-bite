package com.cts.repository;

import com.cts.entity.Order;
import com.cts.entity.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer> {
    OrderAddress findByOrder(Order order);
}