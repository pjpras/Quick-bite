package com.cts.controller;

import com.cts.dto.FoodFeedbackRequestDTO;
import com.cts.dto.FoodFeedbackResponseDTO;
import com.cts.dto.RatingResponseDTO;
import com.cts.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private FoodFeedbackRequestDTO feedbackRequest;
    private FoodFeedbackResponseDTO feedbackResponse;

    @BeforeEach
    void setUp() {
        feedbackRequest = new FoodFeedbackRequestDTO();
        feedbackRequest.setFoodRating(5);
        feedbackRequest.setOrderId(101);

        feedbackResponse = new FoodFeedbackResponseDTO();
    }

    @Test
    @DisplayName("Positive: Submit feedback successfully")
    void submitFeedback_Success() {
        when(feedbackService.submitFoodFeedback(any(FoodFeedbackRequestDTO.class), anyInt()))
                .thenReturn(feedbackResponse);

        ResponseEntity<?> response = feedbackController.submitFeedback(feedbackRequest, 1);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Negative: Check if rating is given - not given")
    void isFoodRatingGiven_NotGiven() {
        when(feedbackService.isFoodRatingGiven(anyInt(), anyLong(), anyInt()))
                .thenReturn(false);

        ResponseEntity<RatingResponseDTO> response = feedbackController.isFoodRatingGiven(1, 101, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
    }
}
