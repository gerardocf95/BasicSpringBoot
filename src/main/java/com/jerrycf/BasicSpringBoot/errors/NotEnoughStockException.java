package com.jerrycf.BasicSpringBoot.errors;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String message) {

        super(message);
    }
}
