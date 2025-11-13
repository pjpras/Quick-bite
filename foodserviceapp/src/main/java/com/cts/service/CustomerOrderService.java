package com.cts.service;

import com.cts.entity.*;
import com.cts.repository.*;
import com.cts.client.AuthServiceClient;
import com.cts.dto.*;
import com.cts.enums.OrderStatus;
import com.cts.exception.ResourceNotFoundException;
import com.cts.exception.UnauthorizedActionException;
import com.cts.model.User;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomerOrderService {
    
    private final OrdersRepository orderRepository;
    private final FoodRepository foodRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final CommonOrderService commonService;
    private final ModelMapper mapper;
    private final AdminOrderService adminOrderService;
    private AuthServiceClient authServiceClient;


	public CustomerOrderService(OrdersRepository orderRepository, FoodRepository foodRepository,
			OrderItemRepository orderItemRepository, OrderAddressRepository orderAddressRepository,
			CommonOrderService commonService, ModelMapper mapper, AdminOrderService adminOrderService,
			AuthServiceClient authServiceClient) {
		super();
		this.orderRepository = orderRepository;
		this.foodRepository = foodRepository;
		this.orderItemRepository = orderItemRepository;
		this.orderAddressRepository = orderAddressRepository;
		this.commonService = commonService;
		this.mapper = mapper;
		this.adminOrderService = adminOrderService;
		this.authServiceClient = authServiceClient;
	}



	@Transactional
    public OrderPlacementResponseDTO placeOrder(OrderPlacementRequestDTO request) {
        // checking the out of stock status of each food item in the order
        request.getItems().forEach(itemDto -> {
            Food food = foodRepository.findById(itemDto.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + itemDto.getFoodId()));
            if (food.isStatus() == false) {
                throw new IllegalStateException("Food item " + food.getName() + " is out of stock.");
            }
        });

        com.cts.model.User customer = commonService.getCurrentAuthenticatedUser();
        if (!(customer instanceof com.cts.model.Customer)) {
            throw new UnauthorizedActionException("Only customers can place orders.");
        }
        Order order = new Order();
        order.setCustomer(customer.getId());
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        
        
        
        String otp = String.format("%06d", (int)(Math.random() * 1000000));
        order.setOtp(otp);
        
        double totalPrice = 0.0;
        int totalQty = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDTO itemDto : request.getItems()) {
            Food food = foodRepository.findById(itemDto.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + itemDto.getFoodId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(food);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(food.getPrice());

            totalPrice += food.getPrice() * itemDto.getQuantity();
            totalQty += itemDto.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setTotalQty(totalQty);
        Order savedOrder = orderRepository.save(order);
        
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setFirstName(request.getAddress().getFirstName());
        orderAddress.setLastName(request.getAddress().getLastName());
        orderAddress.setStreet(request.getAddress().getStreet());
        orderAddress.setCity(request.getAddress().getCity());
        orderAddress.setState(request.getAddress().getState());
        orderAddress.setPin(request.getAddress().getPin());
        orderAddress.setPhoneNo(request.getAddress().getPhoneNo());
        orderAddress.setOrder(savedOrder);
        orderAddressRepository.save(orderAddress);
        
       
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        
        

        ResponseEntity<List<UserResponseDTO>> response = authServiceClient.getActiveDeliveryPartners();
        List<UserResponseDTO> CanBeAssignedpartnersList = new ArrayList<>();
        
        for(UserResponseDTO partner : response.getBody()) {
			System.out.println("Active Delivery Partner: " + partner.getId() + " - " + partner.getName());
			if(partner.getAvailabilityStatus()==true) {
				CanBeAssignedpartnersList.add(partner);
			}
		}
        if(CanBeAssignedpartnersList.isEmpty()){
            throw new ResourceNotFoundException("No available delivery partners at the moment. Please try again later.");
        }
    
        adminOrderService.assignDeliveryPartner(order.getId(), CanBeAssignedpartnersList.get(0).getId());

        return mapper.map(savedOrder, OrderPlacementResponseDTO.class);
    }
    


    @Transactional
    public OrderPlacementResponseDTO cancelOrder(int orderId) {
        com.cts.model.User customer = commonService.getCurrentAuthenticatedUser();
        Order order = commonService.getOrderById(orderId);
        if (order.getCustomer() != customer.getId()) {
            throw new UnauthorizedActionException("You can only cancel your own orders.");
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERED || 
            order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order status is " + 
                order.getOrderStatus().getDisplayName() + ". Cannot cancel.");
        }
        if(order.getOrderStatus() == OrderStatus.OUT) {
        	throw new IllegalStateException("Order is out for delivery. Cannot cancel at this stage.");

        }
        if(order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.CANCELLED);
        }
        Order savedOrder = orderRepository.save(order);
        authServiceClient.updateDeliveryPartnerAvailability(order.getDeliveryPartner(), true);
        return mapper.map(savedOrder, OrderPlacementResponseDTO.class);
    }
    



    public List<OrderResponseDTO> getMyOrders() {
        com.cts.model.User customer = commonService.getCurrentAuthenticatedUser();
        List<Order> orders = commonService.getOrdersByCustomer(customer);
        return orders.stream()
            .map(order -> mapToOrderResponseDto(order))
            .collect(Collectors.toList());
    }
    



    public Order getMyOrder(int orderId) {
        com.cts.model.User customer = commonService.getCurrentAuthenticatedUser();
        Order order = commonService.getOrderById(orderId);
        if (order.getCustomer() != customer.getId()) {
            throw new UnauthorizedActionException("You can only view your own orders.");
        }
        return order;
    }
    


    private OrderResponseDTO mapToOrderResponseDto(Order order) {
        return commonService.mapToOrderResponseDto(order);
    }
}
