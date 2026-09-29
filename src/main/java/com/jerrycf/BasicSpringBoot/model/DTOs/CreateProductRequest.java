package com.jerrycf.BasicSpringBoot.model.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotNull(message = "Product name is required")
        String name,

        @PositiveOrZero(message = "Price must be positive or zero")
        BigDecimal price,

        @PositiveOrZero(message = "Stock must be positive or zero")
        Integer stock
) {
}
