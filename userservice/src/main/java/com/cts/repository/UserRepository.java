package com.cts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.cts.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	


	List<User> findByRoleAndStatusTrue(String role);

	List<User> findByRoleAndStatus(String role, boolean status);

	List<User> findByRoleAndStatusTrueAndNameContainingIgnoreCase(String role, String name);

	@Query("SELECT u FROM User u WHERE Type(u) = DeliveryPartner")
	List<User> findAllDeliveryPartners();

	
}
