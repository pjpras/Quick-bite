package com.cts.service;

import com.cts.dto.OrderPlacementResponseDTO;
import com.cts.entity.Order;
import com.cts.enums.OrderStatus;
import com.cts.model.Admin;
import com.cts.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminOrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private CommonOrderService commonService;

    @Mock
    private ModelMapper mapper;

    private AdminOrderService adminOrderService;

    private Order order;
    private Admin admin;
    private OrderPlacementResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        adminOrderService = new AdminOrderService(
                orderRepository,
                commonService,
                mapper,
                null);

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@test.com");

        order = new Order();
        order.setId(101);
        order.setOrderStatus(OrderStatus.PENDING);

        responseDTO = new OrderPlacementResponseDTO();
        responseDTO.setId(101);
    }

    @Test
    @DisplayName("Positive: Update order status successfully")
    void updateOrderStatus_Success() {
        when(commonService.getCurrentAuthenticatedUser()).thenReturn(admin);
        when(commonService.getOrderById(101)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.map(any(Order.class), any())).thenReturn(responseDTO);

        OrderPlacementResponseDTO result = adminOrderService.updateOrderStatus(101, OrderStatus.DELIVERED);

        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    @DisplayName("Negative: Get any order not found")
    void getAnyOrder_NotFound() {
        when(commonService.getCurrentAuthenticatedUser()).thenReturn(admin);
        when(commonService.getOrderById(999))
                .thenThrow(new com.cts.exception.ResourceNotFoundException("Order not found"));

        assertThrows(com.cts.exception.ResourceNotFoundException.class, () -> {
            adminOrderService.getAnyOrder(999);
        });
    }
}
