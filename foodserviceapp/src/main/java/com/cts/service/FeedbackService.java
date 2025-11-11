package com.cts.service;

import java.util.List;

import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.dto.FoodFeedbackResponseDTO;
import com.cts.entity.Feedback;
import com.cts.entity.Order;

public interface FeedbackService {
	FoodFeedbackResponseDTO submitFoodFeedback(FoodFeedbackRequestDTO foodFeedbackRequestDTO,Integer foodId);
	FoodFeedbackResponseDTO convertToFoodFeedbackDTO(Feedback feedback);
	List<FoodFeedbackResponseDTO> getFeedbackByFoodId(Integer foodId);
	List<Order> getCompletedOrdersForFeedback();
    boolean isFoodRatingGiven(int orderId, long customerId, int foodId);


}
