package com.cts.service;

import com.cts.dto.OrderAddressDTO;
import com.cts.dto.OrderItemResponseDTO;
import com.cts.dto.OrderResponseDTO;
import com.cts.entity.Order;
import com.cts.entity.OrderAddress;
import com.cts.entity.OrderItem;
import com.cts.repository.OrderAddressRepository;
import com.cts.repository.OrderItemRepository;
import com.cts.repository.OrdersRepository;
import com.cts.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommonOrderService {
    
    private final OrdersRepository orderRepository;
    private final UserService userService;
    private final OrderAddressRepository orderAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper mapper;
    
    public CommonOrderService(
        OrdersRepository orderRepository, 
        UserService userService,
        OrderAddressRepository orderAddressRepository,
        OrderItemRepository orderItemRepository,
        ModelMapper mapper
    ) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderAddressRepository = orderAddressRepository;
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
    }
    
   
    public com.cts.model.User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        com.cts.model.User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return user;
    }
    

    public com.cts.model.User getUserById(Long id) {
        com.cts.model.User user = userService.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return user;
    }
    

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }
    

    public List<Order> getAllOrders() {
        Sort sort = Sort.by(
            Sort.Order.desc("orderDate"),
            Sort.Order.desc("orderTime")
        );
        return orderRepository.findAll(sort);
    }
    

    public List<Order> getOrdersByCustomer(com.cts.model.User customer) {
        return orderRepository.findByCustomer(customer.getId());
    }
    

    public List<Order> getOrdersByDeliveryPartner(com.cts.model.User partner) {
        return orderRepository.findByDeliveryPartnerOrderByOrderDateDesc(partner.getId());
    }
    
   
    public List<Order> getOrdersForCurrentUser() {
        com.cts.model.User user = getCurrentAuthenticatedUser();
        
        if (user instanceof com.cts.model.Admin) {
            return getAllOrders();
        } else if (user instanceof com.cts.model.Customer) {
            return getOrdersByCustomer(user);
        } else if (user instanceof com.cts.model.DeliveryPartner) {
            return getOrdersByDeliveryPartner(user);
        } else {
            throw new ResourceNotFoundException("Invalid user role");
        }
    }
    
   
    public List<OrderResponseDTO> getAllOrdersDtoForCurrentUser() {
        List<Order> orders = getOrdersForCurrentUser();
        
        return orders.stream()
            .map(this::mapToOrderResponseDto)
            .collect(Collectors.toList());
    }
    
    public OrderResponseDTO mapToOrderResponseDto(Order order) {
        OrderResponseDTO dto = mapper.map(order, OrderResponseDTO.class);
        
        
        if (order.getCustomer() != 0) {
            com.cts.model.User customer = userService.getUserById(order.getCustomer());
            if (customer != null) {
                dto.setCustomerId(Math.toIntExact(customer.getId()));
                dto.setCustomerName(customer.getName());
            }
        }
        
       
        if (order.getDeliveryPartner() != 0) {
            com.cts.model.User deliveryPartner = userService.getUserById(order.getDeliveryPartner());
            if (deliveryPartner != null) {
                dto.setDeliveryPartnerId(Math.toIntExact(deliveryPartner.getId()));
                dto.setAssignDeliveryPerson(deliveryPartner.getName());
            }
        }
       
        OrderAddress orderAddress = orderAddressRepository.findByOrder(order);
        if (orderAddress != null) {
            dto.setOrderAddress(mapper.map(orderAddress, OrderAddressDTO.class));
        }
        
       
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        if (!orderItems.isEmpty()) {
            List<OrderItemResponseDTO> itemDTOs = orderItems.stream()
                .map(item -> {
                    OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                    itemDTO.setId(item.getId());
                    if (item.getFood() != null) {
                        itemDTO.setFoodId(item.getFood().getId());
                        itemDTO.setFoodName(item.getFood().getName());
                    }
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setSubtotal(item.getQuantity() * item.getPrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());
            dto.setOrderItems(itemDTOs);
        }
        
        return dto;
    }
}
