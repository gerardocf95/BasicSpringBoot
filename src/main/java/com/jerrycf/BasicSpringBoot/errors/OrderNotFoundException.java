package com.jerrycf.BasicSpringBoot.errors;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
