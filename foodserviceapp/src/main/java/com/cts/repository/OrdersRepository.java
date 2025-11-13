package com.cts.repository;

import com.cts.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByCustomer(Long customerId);
    @Query("SELECT o FROM Order o WHERE o.deliveryPartner = :deliveryPartnerId ORDER BY o.orderDate DESC, o.orderTime DESC")
    List<Order> findByDeliveryPartnerOrderByOrderDateDesc(@Param("deliveryPartnerId") Long deliveryPartnerId);
}