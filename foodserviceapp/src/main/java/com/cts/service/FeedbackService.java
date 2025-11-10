package com.cts.service;

import java.util.List;

import com.cts.dto.DeliveryPartnerFeedbackRequestDTO;
import com.cts.dto.DeliveryPartnerFeedbackResponseDTO;
import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.dto.FoodFeedbackResponseDTO;
import com.cts.entity.Feedback;
import com.cts.entity.Order;

public interface FeedbackService {
	FoodFeedbackResponseDTO submitFoodFeedback(FoodFeedbackRequestDTO foodFeedbackRequestDTO,Integer foodId);
	FoodFeedbackResponseDTO convertToFoodFeedbackDTO(Feedback feedback);
	DeliveryPartnerFeedbackResponseDTO submitDeliveryPartnerFeedback(DeliveryPartnerFeedbackRequestDTO 
			deliveryPartnerFeedbackRequestDTO,Long deliveryPartnerId);
	DeliveryPartnerFeedbackResponseDTO convertToDeliveryPartnerFeedbackResponseDTO(Feedback feedback);
	List<FoodFeedbackResponseDTO> getFeedbackByFoodId(Integer foodId);
	List<DeliveryPartnerFeedbackResponseDTO> getFeedbackByDeliveryPartnerId(Long partnerId);
	List<Order> getCompletedOrdersForFeedback();
    boolean isFoodRatingGiven(int orderId, long customerId, int foodId);
    boolean isDeliveryPartnerRatingGiven(int orderId, long customerId, long partnerId);

}
