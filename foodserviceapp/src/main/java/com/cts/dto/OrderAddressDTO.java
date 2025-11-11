package com.cts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OrderAddressDTO {

	@NotBlank(message = "First name is required")
	@Size(min = 4, message = "First name must be at least 4 characters long")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 4, message = "Last name must be at least 4 characters long")
    private String lastName;
    @NotBlank(message = "Street is required")
    @Size(min = 4, message = "Street must be at least 4 characters long")
    private String street;
	@NotBlank(message = "City is required")
    private String city;
	@NotBlank(message = "State is required")
    private String state;
	@NotBlank(message = "Pin is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "PIN code must be exactly 6 digits")
    private String pin;
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNo;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


}