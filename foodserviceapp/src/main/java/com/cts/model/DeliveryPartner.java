package com.cts.model;

public class DeliveryPartner extends User {
    private int rating;
    
    public DeliveryPartner() {
        super();
    }
    
    public DeliveryPartner(Long id, String email, String name) {
        super(id, email, name, "deliveryPartner");
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}