package com.cts.dto;

import java.time.LocalDate;

public class FoodFeedbackResponseDTO {
    private Long feedbackId;
    private Integer foodRating;
    private LocalDate dateOfFeedback;
    private Integer foodId;
    private Integer orderId;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

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

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
