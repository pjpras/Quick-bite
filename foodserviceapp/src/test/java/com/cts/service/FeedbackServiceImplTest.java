package com.cts.service;

import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.entity.Feedback;
import com.cts.entity.Food;
import com.cts.entity.Order;
import com.cts.entity.OrderItem;
import com.cts.enums.OrderStatus;
import com.cts.model.User;
import com.cts.repository.FeedbackRepository;
import com.cts.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserService userService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private CommonOrderService commonOrderService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private FeedbackServiceImpl feedbackService;

    private User user;
    private Food food;
    private Order order;
    private OrderItem orderItem;
    private FoodFeedbackRequestDTO feedbackRequest;
    private Feedback feedback;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackServiceImpl();
        try {
            java.lang.reflect.Field feedbackRepoField = FeedbackServiceImpl.class.getDeclaredField("feedbackRepo");
            feedbackRepoField.setAccessible(true);
            feedbackRepoField.set(feedbackService, feedbackRepository);

            java.lang.reflect.Field userServiceField = FeedbackServiceImpl.class.getDeclaredField("userService");
            userServiceField.setAccessible(true);
            userServiceField.set(feedbackService, userService);

            java.lang.reflect.Field foodRepoField = FeedbackServiceImpl.class.getDeclaredField("foodRepo");
            foodRepoField.setAccessible(true);
            foodRepoField.set(feedbackService, foodRepository);

            java.lang.reflect.Field commonOrderServiceField = FeedbackServiceImpl.class
                    .getDeclaredField("commonOrderService");
            commonOrderServiceField.setAccessible(true);
            commonOrderServiceField.set(feedbackService, commonOrderService);

            java.lang.reflect.Field modelMapperField = FeedbackServiceImpl.class.getDeclaredField("modelMapper");
            modelMapperField.setAccessible(true);
            modelMapperField.set(feedbackService, modelMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        food = new Food();
        food.setId(1);
        food.setName("Pizza");

        orderItem = new OrderItem();
        orderItem.setFood(food);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        order = new Order();
        order.setId(101);
        order.setCustomer(1L);
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setOrderItems(orderItems);

        feedbackRequest = new FoodFeedbackRequestDTO();
        feedbackRequest.setOrderId(101);
        feedbackRequest.setFoodRating(5);

        feedback = new Feedback();
        feedback.setCustomer(1L);
    }

    @Test
    @DisplayName("Positive: Check if food rating is given")
    void isFoodRatingGiven_Success() {
        when(feedbackRepository.existsByOrderIdAndCustomerAndFoodId(101, 1L, 1))
                .thenReturn(true);

        Boolean result = feedbackService.isFoodRatingGiven(101, 1L, 1);

        assertTrue(result);
    }

    @Test
    @DisplayName("Negative: Check if food rating is given - not given")
    void isFoodRatingGiven_NotGiven() {
        when(feedbackRepository.existsByOrderIdAndCustomerAndFoodId(101, 1L, 1))
                .thenReturn(false);

        Boolean result = feedbackService.isFoodRatingGiven(101, 1L, 1);

        assertFalse(result);
    }
}
