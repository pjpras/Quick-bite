package com.cts.controller;

import com.cts.dto.DeliveryPartnerFeedbackRequestDTO;
import com.cts.dto.DeliveryPartnerFeedbackResponseDTO;
import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.dto.FoodFeedbackResponseDTO;
import com.cts.dto.RatingResponseDTO;
import com.cts.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Feedback Controller", description = "Operations related to feedback")
@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/food/{foodId}")
    @Operation(summary = "Submit feedback for food", description = "Returns the saved feedback details.")
    public ResponseEntity<?> submitFeedback(
            @Valid @RequestBody FoodFeedbackRequestDTO foodFeedbackRequestDTO,
            @PathVariable int foodId) {

        Object response = feedbackService.submitFoodFeedback(foodFeedbackRequestDTO, foodId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/partner/{deliveryPartnerId}")
    @Operation(summary = "Submit feedback for delivery partner", description = "Returns the saved feedback details.")
    public ResponseEntity<?> submitFeedback(
            @Valid @RequestBody DeliveryPartnerFeedbackRequestDTO deliveryPartnerFeedbackRequestDTO,
            @PathVariable Long deliveryPartnerId) {

        Object response = feedbackService.submitDeliveryPartnerFeedback(deliveryPartnerFeedbackRequestDTO, deliveryPartnerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   @GetMapping("/food/{foodId}")
   @Operation(summary = "Get feedback by food ID", description = "Returns all feedback for a specific food item.")
  public ResponseEntity<List<FoodFeedbackResponseDTO>> getFeedbackByFood(@PathVariable Integer foodId) {
      List<FoodFeedbackResponseDTO> feedbacks = feedbackService.getFeedbackByFoodId(foodId);
        return ResponseEntity.ok(feedbacks);
    }

 @GetMapping("/partner/{partnerId}")
@Operation(summary = "Get feedback by partner ID", description = "Returns all feedback for a specific delivery partner.")
 public ResponseEntity<List<DeliveryPartnerFeedbackResponseDTO>> getFeedbackByPartner(@PathVariable Long partnerId) {
       List<DeliveryPartnerFeedbackResponseDTO> feedbacks = feedbackService.getFeedbackByDeliveryPartnerId(partnerId);
   return ResponseEntity.ok(feedbacks);
   }
   
   @GetMapping("/completed-orders")
   @Operation(summary = "Get completed orders for feedback", description = "Returns all completed orders that the customer can provide feedback on.")
   public ResponseEntity<List<Order>> getCompletedOrdersForFeedback() {
       List<Order> completedOrders = feedbackService.getCompletedOrdersForFeedback();
       return ResponseEntity.ok(completedOrders);
   }
   
   @GetMapping("Rating/status/food")
   @Operation(summary = "Get Whether rating is given or not", description = "Returns the true if rating is given.")
   public ResponseEntity<RatingResponseDTO> isFoodRatingGiven(
           @RequestParam int foodId, 
           @RequestParam int orderId,
           @RequestParam Long customerId) {
       Boolean isGiven = feedbackService.isFoodRatingGiven(orderId, customerId, foodId);
       RatingResponseDTO response = new RatingResponseDTO();
       response.setStatus(isGiven);
       return ResponseEntity.ok(response);
   }
   
   @GetMapping("Rating/status/delivery-partner")
   @Operation(summary = "Check if delivery partner rating is given", description = "Returns true if rating is given for a delivery partner in an order.")
   public ResponseEntity<RatingResponseDTO> isDeliveryPartnerRatingGiven(
           @RequestParam Long partnerId, 
           @RequestParam int orderId,
           @RequestParam Long customerId) {
       Boolean isGiven = feedbackService.isDeliveryPartnerRatingGiven(orderId, customerId, partnerId);
       RatingResponseDTO response = new RatingResponseDTO();
       response.setStatus(isGiven);
       return ResponseEntity.ok(response);
   }
}