package com.cts.exception;

public class FoodAlreadyExistsException extends RuntimeException {
    public FoodAlreadyExistsException(String message) {
        super(message);
    }
}
