package com.cts.model;

public class Admin extends User {
    
    public Admin() {
        super();
    }
    
    public Admin(Long id, String email, String name) {
        super(id, email, name, "admin");
    }
}