package com.cts.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue(value = "deliveryPartner")

public class DeliveryPartner extends User{

}
