package com.cts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterCustomerRequestDTO {
	@NotBlank(message = "Email is required")
    @Email(message = "Email id is invalid format")
    private String email;

	@NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

	@NotBlank(message = "Name is required")
    @Size(min = 2, max = 15, message = "Name must be between 2 and 15 characters")
    private String name;

	@NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phno;
     @NotBlank(message = "Location is required")
    @Size(min = 2, max = 20, message = "Location must be between 2 and 20 characters")
    private String location;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhno() {
		return phno;
	}
	public void setPhno(String phno) {
		this.phno = phno;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

    

}
