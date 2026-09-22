package com.jerrycf.BasicSpringBoot.controller;


import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findAllProducts(){
        return productService.listAllProducts();
    }

    @GetMapping("/cheap")
    public List<Product> findProductsWithPriceLimit(@RequestParam Double limit) {
        return productService.listCheaper(limit);
    }

    @GetMapping("/products/{id}")
    public Optional<Product> findProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.create(product);
    }


}
