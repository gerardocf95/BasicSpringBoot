package com.jerrycf.BasicSpringBoot.model.DTOs;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        BigDecimal price,
        int stock
) {
}
