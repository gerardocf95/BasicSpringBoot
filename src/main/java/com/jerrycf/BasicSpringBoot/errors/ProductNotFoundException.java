package com.jerrycf.BasicSpringBoot.errors;

public class ProductNotFoundException extends ResourceNotFoundException {
    public ProductNotFoundException(Long id) {
        super("Product not found with id " + id);
    }

    public ProductNotFoundException(String message){
        super(message);
    }


}
