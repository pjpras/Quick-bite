package com.cts.controller;

import com.cts.entity.Order;
import com.cts.dto.OrderPlacementRequestDTO;
import com.cts.dto.OrderPlacementResponseDTO;
import com.cts.dto.OrderResponseDTO;
import com.cts.enums.OrderStatus;
import com.cts.service.CustomerOrderService;
import com.cts.service.AdminOrderService;
import com.cts.service.DeliveryPartnerOrderService;

import jakarta.validation.Valid;

import com.cts.service.CommonOrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/orders")
public class FoodOrderController {

    private final CustomerOrderService customerOrderService;
    private final AdminOrderService adminOrderService;
    private final DeliveryPartnerOrderService deliveryPartnerOrderService;
    private final CommonOrderService commonOrderService;

    public FoodOrderController(
        CustomerOrderService customerOrderService,
        AdminOrderService adminOrderService,
        DeliveryPartnerOrderService deliveryPartnerOrderService,
        CommonOrderService commonOrderService
    ) {
        this.customerOrderService = customerOrderService;
        this.adminOrderService = adminOrderService;
        this.deliveryPartnerOrderService = deliveryPartnerOrderService;
        this.commonOrderService = commonOrderService;
    }



    @PostMapping("/place")
    public ResponseEntity<OrderPlacementResponseDTO> placeOrder(@Valid @RequestBody OrderPlacementRequestDTO request) {
        OrderPlacementResponseDTO response = customerOrderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
 
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getMyOrder(@PathVariable int orderId) {
        Order order = customerOrderService.getMyOrder(orderId);
        return ResponseEntity.ok(order);
    }
    


    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> viewAllOrders() {
        List<OrderResponseDTO> orders = commonOrderService.getAllOrdersDtoForCurrentUser();
        return ResponseEntity.ok(orders);
    }
    
  
    @PutMapping("/status/{orderId}")
    public ResponseEntity<OrderPlacementResponseDTO> updateStatus(
        @PathVariable int orderId, 
        @RequestParam OrderStatus newStatus
    ) {
        OrderPlacementResponseDTO updatedOrder = adminOrderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderPlacementResponseDTO> cancelOrder(@PathVariable int orderId) {
        OrderPlacementResponseDTO response = customerOrderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }

  
    @PutMapping("/assign/{orderId}")
    public ResponseEntity<OrderPlacementResponseDTO> assignPartner(
        @PathVariable int orderId, 
        @RequestParam Long partnerId
    ) {
        OrderPlacementResponseDTO assignedOrder = adminOrderService.assignDeliveryPartner(orderId, partnerId);
        return ResponseEntity.ok(assignedOrder);
    }

  
    @PatchMapping("/Deliverypartner/ordercompleted")
    public ResponseEntity<OrderPlacementResponseDTO> markOrderCompleted(
        @RequestParam int orderId,
        @RequestParam String otp
    ) {
        OrderPlacementResponseDTO completedOrder = deliveryPartnerOrderService.markOrderCompleted(orderId, otp);
        return ResponseEntity.ok(completedOrder);
    }

     @PatchMapping("/Deliverypartner/outfordelivery")
    public ResponseEntity<OrderPlacementResponseDTO> markOrderOutForDelivery(
        @RequestParam int orderId
    ) {
        OrderPlacementResponseDTO completedOrder = deliveryPartnerOrderService.markOrderOutForDelivery(orderId);
        return ResponseEntity.ok(completedOrder);
    }
}