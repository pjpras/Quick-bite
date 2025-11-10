package com.cts.dto;
import java.time.LocalDate;
public class DeliveryPartnerFeedbackResponseDTO {
    private Long feedbackId;
    private Integer partnerRating;
    private LocalDate dateOfFeedback;
    private Long partnerId;
    private Integer orderId;
    

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getPartnerRating() {
        return partnerRating;
    }

    public void setPartnerRating(Integer partnerRating) {
        this.partnerRating = partnerRating;
    }

    public LocalDate getDateOfFeedback() {
        return dateOfFeedback;
    }

    public void setDateOfFeedback(LocalDate dateOfFeedback) {
        this.dateOfFeedback = dateOfFeedback;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
    
}