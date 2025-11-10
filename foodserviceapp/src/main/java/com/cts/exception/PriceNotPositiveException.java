package com.cts.exception;

public class PriceNotPositiveException extends RuntimeException {
    public PriceNotPositiveException(String message) {
        super(message);
    }
}
