package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        BigDecimal price,
        Integer stock
) {
    public static ProductResponse from(Product product){
        return new ProductResponse(
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
