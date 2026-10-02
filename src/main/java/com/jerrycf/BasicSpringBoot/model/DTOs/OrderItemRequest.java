package com.jerrycf.BasicSpringBoot.model.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull
        Long productId,

        @NotNull
        @Positive(message = "Quantity is required")
        Integer quantity
) {
}
