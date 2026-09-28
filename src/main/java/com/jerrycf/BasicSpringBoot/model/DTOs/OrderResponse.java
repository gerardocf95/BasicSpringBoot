package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.Order;
import com.jerrycf.BasicSpringBoot.model.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long clientId,
        List<OrderItem> orderItems,
        Double totalPrice,
        String details,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getClientId(),
            order.getOrderItems(),
            order.getTotalPrice(),
            order.getDetails(),
            LocalDateTime.now()
        );
    }
}
