package com.cts.service;

import com.cts.entity.Order;
import com.cts.model.User;
import com.cts.repository.OrderAddressRepository;
import com.cts.repository.OrderItemRepository;
import com.cts.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommonOrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private OrderAddressRepository orderAddressRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ModelMapper mapper;

    private CommonOrderService commonOrderService;

    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        commonOrderService = new CommonOrderService(
                orderRepository,
                userService,
                orderAddressRepository,
                orderItemRepository,
                mapper);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        order = new Order();
        order.setId(101);
        order.setCustomer(1L);
    }

    @Test
    @DisplayName("Positive: Get order by ID successfully")
    void getOrderById_Success() {
        when(orderRepository.findById(101)).thenReturn(Optional.of(order));

        Order result = commonOrderService.getOrderById(101);

        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    @DisplayName("Negative: Get order by ID not found")
    void getOrderById_NotFound() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(com.cts.exception.ResourceNotFoundException.class, () -> {
            commonOrderService.getOrderById(999);
        });
    }
}
