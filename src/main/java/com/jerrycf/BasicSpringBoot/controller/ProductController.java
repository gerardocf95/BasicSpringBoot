package com.jerrycf.BasicSpringBoot.controller;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateProductRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.ProductResponse;
import com.jerrycf.BasicSpringBoot.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*** GET ***/
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts(){
        return ResponseEntity.ok(productService.listAllProducts());
    }

    @GetMapping("/cheap")
    public ResponseEntity<List<ProductResponse>> findProductsWithPriceLimit(@RequestParam BigDecimal limit) {
        return ResponseEntity.ok(productService.listCheaper(limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /*** POST ***/
    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody CreateProductRequest createProductRequest) {
        ProductResponse response = productService.create(createProductRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }


    /*** DELETE ***/
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
