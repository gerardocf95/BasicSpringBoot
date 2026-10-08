package com.jerrycf.BasicSpringBoot.model.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(

        @NotNull
        Long clientId,

        @Nullable
        @Size(max = 500)
        String details,

        @NotEmpty
        @Valid
        List<OrderItemRequest> orderItems

) {
}
