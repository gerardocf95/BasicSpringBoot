package com.jerrycf.BasicSpringBoot.service;


import com.jerrycf.BasicSpringBoot.errors.ProductNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateProductRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.ProductResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*** GET ***/
    @Transactional(readOnly = true)
    public List<ProductResponse> listAllProducts(){

        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listCheaper(BigDecimal limit) {
        return productRepository.findByPriceLessThan(limit).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id).map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /*** POST ***/
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price().setScale(2, RoundingMode.HALF_UP));
        product.setStock(request.stock());
        return ProductResponse.from(productRepository.save(product));
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
