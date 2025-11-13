package com.cts.controller;

import com.cts.dto.OrderPlacementRequestDTO;
import com.cts.dto.OrderPlacementResponseDTO;
import com.cts.entity.Order;
import com.cts.service.AdminOrderService;
import com.cts.service.CommonOrderService;
import com.cts.service.CustomerOrderService;
import com.cts.service.DeliveryPartnerOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodOrderControllerTest {

    @Mock
    private CustomerOrderService customerOrderService;

    @Mock
    private AdminOrderService adminOrderService;

    @Mock
    private DeliveryPartnerOrderService deliveryPartnerOrderService;

    @Mock
    private CommonOrderService commonOrderService;

    private FoodOrderController foodOrderController;

    private OrderPlacementRequestDTO orderRequest;
    private OrderPlacementResponseDTO orderResponse;
    private Order order;

    @BeforeEach
    void setUp() {
        foodOrderController = new FoodOrderController(
                customerOrderService,
                adminOrderService,
                deliveryPartnerOrderService,
                commonOrderService);

        orderRequest = new OrderPlacementRequestDTO();

        orderResponse = new OrderPlacementResponseDTO();
        orderResponse.setId(101);

        order = new Order();
        order.setId(101);
        order.setCustomer(1L);
    }

    @Test
    @DisplayName("Positive: Place order successfully")
    void placeOrder_Success() {
        when(customerOrderService.placeOrder(any(OrderPlacementRequestDTO.class)))
                .thenReturn(orderResponse);

        ResponseEntity<OrderPlacementResponseDTO> response = foodOrderController.placeOrder(orderRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(101, response.getBody().getId());
    }

    @Test
    @DisplayName("Negative: Get order by ID not found")
    void getMyOrder_NotFound() {
        when(customerOrderService.getMyOrder(999)).thenReturn(null);

        ResponseEntity<Order> response = foodOrderController.getMyOrder(999);

        assertNull(response.getBody());
    }
}
