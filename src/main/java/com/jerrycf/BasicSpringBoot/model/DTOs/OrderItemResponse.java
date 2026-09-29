package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {
    public static OrderItemResponse from(OrderItem orderItem){
        return new OrderItemResponse(
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }
}
