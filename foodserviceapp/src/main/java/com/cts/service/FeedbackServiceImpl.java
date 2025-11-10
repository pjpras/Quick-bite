package com.cts.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.dto.DeliveryPartnerFeedbackRequestDTO;
import com.cts.dto.DeliveryPartnerFeedbackResponseDTO;
import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.dto.FoodFeedbackResponseDTO;
import com.cts.entity.Order;
import com.cts.enums.OrderStatus;
import com.cts.exception.PartnerIdNotFoundException;
import com.cts.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cts.entity.Feedback;
import com.cts.entity.Food;
import com.cts.exception.CustomerNotFoundException;
import com.cts.exception.FoodIdNotFoundException;
import com.cts.model.User;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodRepository foodRepo;
    
    @Autowired
    private CommonOrderService commonOrderService;

    public FoodFeedbackResponseDTO submitFoodFeedback(
            FoodFeedbackRequestDTO foodFeedbackRequestDTO,
            Integer foodId) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new CustomerNotFoundException("User not found with email: " + email);
        }
        long customerId = user.getId();
        int orderId = foodFeedbackRequestDTO.getOrderId();
        
        Order order = commonOrderService.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        if (order.getCustomer() != customerId) {
            throw new RuntimeException("You can only provide feedback for your own orders");
        }
        
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("You can only provide feedback for delivered orders");
        }
        
        boolean foodExistsInOrder = order.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.getFood().getId() == foodId);
        
        if (!foodExistsInOrder) {
            throw new RuntimeException("Food item not found in this order");
        }
        
        if (feedbackRepo.existsByOrderIdAndCustomerAndFoodId(orderId, customerId, foodId)) {
            throw new RuntimeException("Feedback already provided for this food in this order");
        }

        Feedback feedback = new Feedback();
        feedback.setDateOfFeedback(LocalDate.now());
        feedback.setCustomer(customerId);
        feedback.setOrderId(orderId);
        
        Food food = foodRepo.findById(foodId)
                .orElseThrow(() -> new FoodIdNotFoundException("Food not found"));
        feedback.setFood(food);
        feedback.setFoodRating(foodFeedbackRequestDTO.getFoodRating());
        feedback.setPartnerRating(null);
        feedback.setDeliveryPartner(0); 
        Feedback savedFeedback = feedbackRepo.save(feedback);
        
        Float avgRatingDouble = feedbackRepo.findAverageRatingByFoodId(foodId);
        if (avgRatingDouble != null) {
            float avgRating = avgRatingDouble.floatValue();
            food.setAvgRating(avgRating);
            foodRepo.save(food);
        }

        return convertToFoodFeedbackDTO(savedFeedback);
    }


    public FoodFeedbackResponseDTO convertToFoodFeedbackDTO(Feedback feedback) {
        FoodFeedbackResponseDTO dto = modelMapper.map(feedback, FoodFeedbackResponseDTO.class);
        if (feedback.getFood() != null) {
            dto.setFoodId(feedback.getFood().getId());
        } else {
            dto.setFoodId(null);
        }
        if (feedback.getOrderId() != null) {
            dto.setOrderId(feedback.getOrderId());
        } else {
            dto.setOrderId(null);
        }
        return dto;
    }

    public DeliveryPartnerFeedbackResponseDTO submitDeliveryPartnerFeedback(
            DeliveryPartnerFeedbackRequestDTO deliveryPartnerFeedbackRequestDTO,
            Long deliveryPartnerId) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new CustomerNotFoundException("User not found with email: " + email);
        }
        long customerId = user.getId();
        int orderId = deliveryPartnerFeedbackRequestDTO.getOrderId();
        
        Order order = commonOrderService.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        if (order.getCustomer() != customerId) {
            throw new RuntimeException("You can only provide feedback for your own orders");
        }
        
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("You can only provide feedback for delivered orders");
        }
        
        if (order.getDeliveryPartner() == 0 || order.getDeliveryPartner() != deliveryPartnerId) {
            throw new RuntimeException("This delivery partner was not assigned to your order");
        }
        
        if (feedbackRepo.existsByOrderIdAndCustomerAndDeliveryPartner(orderId, customerId, deliveryPartnerId)) {
            throw new RuntimeException("Feedback already provided for this delivery partner in this order");
        }

        Feedback feedback = new Feedback();
        feedback.setDateOfFeedback(LocalDate.now());
        feedback.setCustomer(customerId);
        feedback.setOrderId(orderId);

        User deliveryPartner = userService.getUserById(deliveryPartnerId);
        if (deliveryPartner == null || !"deliveryPartner".equals(deliveryPartner.getRole())) {
            throw new PartnerIdNotFoundException("DeliveryPartner not found");
        }
        feedback.setDeliveryPartner(deliveryPartnerId);
        feedback.setPartnerRating(deliveryPartnerFeedbackRequestDTO.getPartnerRating());
        feedback.setFoodRating(null);
        feedback.setFood(null);

        Feedback savedFeedback = feedbackRepo.save(feedback);
        return convertToDeliveryPartnerFeedbackResponseDTO(savedFeedback);
    }




    public DeliveryPartnerFeedbackResponseDTO convertToDeliveryPartnerFeedbackResponseDTO(Feedback feedback) {
        DeliveryPartnerFeedbackResponseDTO dto = modelMapper.map(feedback, DeliveryPartnerFeedbackResponseDTO.class);
        if (feedback.getDeliveryPartner() != 0) {
            dto.setPartnerId(feedback.getDeliveryPartner());
        } else {
            dto.setPartnerId(null);
        }
        if (feedback.getOrderId() != null) {
            dto.setOrderId(feedback.getOrderId());
        } else {
            dto.setOrderId(null);
        }
        return dto;
    }

   public List<FoodFeedbackResponseDTO> getFeedbackByFoodId(Integer foodId) {
        List<Feedback> feedbacks = feedbackRepo.findByFoodId(foodId);
        if (feedbacks.isEmpty()) {
           throw new FoodIdNotFoundException("No feedback found for Food ID: " + foodId);
       }
        return feedbacks.stream()
                .map(this::convertToFoodFeedbackDTO)
                .collect(java.util.stream.Collectors.toList());
   }

 public List<DeliveryPartnerFeedbackResponseDTO> getFeedbackByDeliveryPartnerId(Long partnerId) {
       
       User deliveryPartner = userService.getUserById(partnerId);
       if (deliveryPartner == null || !"deliveryPartner".equals(deliveryPartner.getRole())) {
           throw new PartnerIdNotFoundException("Partner not found");
       }
     List<Feedback> feedbacks = feedbackRepo.findByDeliveryPartner(partnerId);
      if (feedbacks.isEmpty()) {
         throw new PartnerIdNotFoundException("No feedback found for Partner ID: " + partnerId);
      }
     return feedbacks.stream()
             .map(this::convertToDeliveryPartnerFeedbackResponseDTO)
             .collect(java.util.stream.Collectors.toList());
 }
 
 public List<Order> getCompletedOrdersForFeedback() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     String email = authentication.getName();
     User user = userService.getUserByEmail(email);
     if (user == null) {
         throw new CustomerNotFoundException("User not found with email: " + email);
     }
     
     return commonOrderService.getOrdersByCustomer(user).stream()
             .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
             .collect(java.util.stream.Collectors.toList());
 }
 
 public boolean isFoodRatingGiven(int orderId, long customerId, int foodId) {
     return feedbackRepo.existsByOrderIdAndCustomerAndFoodId(orderId, customerId, foodId);
 }
 
 public boolean isDeliveryPartnerRatingGiven(int orderId, long customerId, long partnerId) {
     return feedbackRepo.existsByOrderIdAndCustomerAndDeliveryPartner(orderId, customerId, partnerId);
 }
  }


