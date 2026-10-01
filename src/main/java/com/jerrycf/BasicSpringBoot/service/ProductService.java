package com.jerrycf.BasicSpringBoot.service;


import com.jerrycf.BasicSpringBoot.errors.ProductNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateProductRequest;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*** GET ***/
    public List<Product> listAllProducts(){

        return productRepository.findAll();
    }

    public List<Product> listCheaper(BigDecimal limit) {
        return productRepository.findByPriceLessThan(limit);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /*** POST ***/
    @Transactional
    public Product create(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        return productRepository.save(product);
    }

    /*** DELETE ***/
    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Transactional
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
