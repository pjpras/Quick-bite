package com.cts.repository;

import com.cts.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByCustomer(Long customerId);
    List<Feedback> findByDeliveryPartner(Long deliveryPartnerId);
    
    List<Feedback> findByFoodId(Integer foodId);
    
    @Query("SELECT AVG(f.foodRating) FROM Feedback f WHERE f.food.id = :foodId")
    Float findAverageRatingByFoodId(@Param("foodId") int foodId);
    
    @Query("SELECT COUNT(f) > 0 FROM Feedback f WHERE f.orderId = :orderId AND f.customer = :customerId AND f.food.id = :foodId")
    boolean existsByOrderIdAndCustomerAndFoodId(@Param("orderId") int orderId, @Param("customerId") long customerId, @Param("foodId") int foodId);
    
    @Query("SELECT COUNT(f) > 0 FROM Feedback f WHERE f.orderId = :orderId AND f.customer = :customerId AND f.deliveryPartner = :partnerId")
    boolean existsByOrderIdAndCustomerAndDeliveryPartner(@Param("orderId") int orderId, @Param("customerId") long customerId, @Param("partnerId") long partnerId);
}