package com.cts.repository;

import com.cts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User,Long> {

        User findByEmail(String email);
}

