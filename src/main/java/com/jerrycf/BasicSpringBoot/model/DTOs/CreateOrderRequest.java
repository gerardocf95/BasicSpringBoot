package com.jerrycf.BasicSpringBoot.model.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotNull
        Long clientId,

        @Nullable
        String details,

        @NotNull
        List<OrderItemRequest> orderItems

) {
}
