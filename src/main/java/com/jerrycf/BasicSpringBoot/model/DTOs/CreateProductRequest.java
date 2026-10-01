package com.jerrycf.BasicSpringBoot.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "Product name is required")
        String name,

        @NotNull
        @Positive(message = "Price must be positive or zero")
        BigDecimal price,

        @NotNull
        @PositiveOrZero(message = "Stock must be positive or zero")
        Integer stock
) {
}
