package com.cts.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;
/* 
    @Column(nullable = true)
    private Integer partnerRating;
*/
    @Column(nullable = true)
    private Integer foodRating;

    private LocalDate dateOfFeedback;

    private Integer orderId;
    
    private long  customer;
/* 
    private long deliveryPartner;
*/
    @ManyToOne
    @JoinColumn(name="food_id", nullable = true)
    private Food food;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

/* 
    public Integer getPartnerRating() {
        return partnerRating;
    }

    public void setPartnerRating(Integer partnerRating) {
        this.partnerRating = partnerRating;
    }
*/
    public Integer getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(Integer foodRating) {
        this.foodRating = foodRating;
    }

    public LocalDate getDateOfFeedback() {
        return dateOfFeedback;
    }

    public void setDateOfFeedback(LocalDate dateOfFeedback) {
        this.dateOfFeedback = dateOfFeedback;
    }


    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

	public long getCustomer() {
		return customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}
/*
	public long getDeliveryPartner() {
		return deliveryPartner;
	}

	public void setDeliveryPartner(long deliveryPartner) {
		this.deliveryPartner = deliveryPartner;
	}
 */
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
    
}
