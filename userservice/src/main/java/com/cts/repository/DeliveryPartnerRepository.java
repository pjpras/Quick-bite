package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.entity.DeliveryPartner;
import com.cts.entity.User;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {

	DeliveryPartner findByEmail(String email);

}
