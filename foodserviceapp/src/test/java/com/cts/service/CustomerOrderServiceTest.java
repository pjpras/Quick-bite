package com.cts.service;

import com.cts.entity.Order;
import com.cts.enums.OrderStatus;
import com.cts.model.Customer;
import com.cts.repository.FoodRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerOrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderAddressRepository orderAddressRepository;

    @Mock
    private CommonOrderService commonService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private AdminOrderService adminOrderService;

    private CustomerOrderService customerOrderService;

    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerOrderService = new CustomerOrderService(
                orderRepository,
                foodRepository,
                orderItemRepository,
                orderAddressRepository,
                commonService,
                mapper,
                adminOrderService,
                null);

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@test.com");

        order = new Order();
        order.setId(101);
        order.setCustomer(1L);
        order.setOrderStatus(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Positive: Get my order successfully")
    void getMyOrder_Success() {
        when(commonService.getCurrentAuthenticatedUser()).thenReturn(customer);
        when(commonService.getOrderById(101)).thenReturn(order);

        Order result = customerOrderService.getMyOrder(101);

        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    @DisplayName("Negative: Get my order not found")
    void getMyOrder_NotFound() {
        when(commonService.getCurrentAuthenticatedUser()).thenReturn(customer);
        when(commonService.getOrderById(999))
                .thenThrow(new com.cts.exception.ResourceNotFoundException("Order not found with ID: 999"));

        assertThrows(com.cts.exception.ResourceNotFoundException.class, () -> {
            customerOrderService.getMyOrder(999);
        });
    }
}
