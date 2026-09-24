package com.jerrycf.BasicSpringBoot.controller;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*** GET ***/
    @GetMapping
    public List<Product> findAllProducts(){
        return productService.listAllProducts();
    }

    @GetMapping("/cheap")
    public List<Product> findProductsWithPriceLimit(@RequestParam Double limit) {
        return productService.listCheaper(limit);
    }

    @GetMapping("/{id}")
    public Product findProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    /*** POST ***/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@Valid @RequestBody Product product) {
        return productService.create(product);
    }


    /*** DELETE ***/
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

}
