package com.cts.exception;

public class FoodIdNotFoundException extends RuntimeException {
	public FoodIdNotFoundException(String message) {
		super(message);
	}
}
