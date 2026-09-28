package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long id,
        Long clientId,
        List<OrderItemResponse> orderItems,
        Double totalPrice,
        String details,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getClientId(),
            order.getOrderItems().stream().map(OrderItemResponse::from).toList(),
            order.getTotalPrice(),
            order.getDetails(),
            order.getCreatedAt()
        );
    }
}
