/* 
package com.cts.dto;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class DeliveryPartnerFeedbackRequestDTO {

    @Min(value = 1, message = "Partner rating must be at least 1")
    @Max(value = 5, message = "Partner rating must be at most 5")
    private Integer partnerRating;

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    public Integer getPartnerRating() {
        return partnerRating;
    }

    public void setPartnerRating(Integer partnerRating) {
        this.partnerRating = partnerRating;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

}
    */