package com.jerrycf.BasicSpringBoot.controller;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateProductRequest;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<List<Product>> findAllProducts(){
        return ResponseEntity.ok(productService.listAllProducts());
    }

    @GetMapping("/cheap")
    public ResponseEntity<List<Product>> findProductsWithPriceLimit(@RequestParam BigDecimal limit) {
        return ResponseEntity.ok(productService.listCheaper(limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /*** POST ***/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> save(@Valid @RequestBody CreateProductRequest product) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build().toUri();
        return ResponseEntity.created(location).body(productService.create(product));
    }


    /*** DELETE ***/
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
