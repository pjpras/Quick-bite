package com.cts.repository;

import com.cts.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    List<Cart> findByUserId(Long userId);
    
    Optional<Cart> findByUserIdAndFoodId(Long userId, int foodId);
    
    @Transactional
    @Modifying
    void deleteByUserIdAndFoodId(Long userId, int foodId);
    
    @Transactional
    @Modifying
    void deleteByUserId(Long userId);
}
