package com.cts.model;

public class Customer extends User {
    
    public Customer() {
        super();
    }
    
    public Customer(Long id, String email, String name) {
        super(id, email, name, "customer");
    }
}