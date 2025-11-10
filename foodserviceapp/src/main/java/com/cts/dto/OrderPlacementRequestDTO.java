package com.cts.dto;

import java.util.List;

public class OrderPlacementRequestDTO { 
    private OrderAddressDTO address;
    private List<OrderItemDTO> items;


    public OrderAddressDTO getAddress() { 
        return address;
     }
    public void setAddress(OrderAddressDTO address) {
         this.address = address; 
        }
    public List<OrderItemDTO> getItems() {
         return items; 
        }
    public void setItems(List<OrderItemDTO> items) { 
        this.items = items; 
    }
}