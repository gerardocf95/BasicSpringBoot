package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.Client;
import com.jerrycf.BasicSpringBoot.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long id,
        Long clientId,
        String clientName,
        List<OrderItemResponse> orderItems,
        BigDecimal totalPrice,
        String details,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getClient().getId(),
                order.getClient().getName(),
                order.getOrderItems().stream().map(OrderItemResponse::from).toList(),
                order.getTotalPrice(),
                order.getDetails(),
                order.getCreatedAt()
        );
    }
}
