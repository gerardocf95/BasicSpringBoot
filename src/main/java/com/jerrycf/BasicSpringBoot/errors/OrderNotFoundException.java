package com.jerrycf.BasicSpringBoot.errors;

public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(Long id) {

        super("Order not found with id: " + id);
    }

    public OrderNotFoundException(String message){
        super(message);
    }
}
