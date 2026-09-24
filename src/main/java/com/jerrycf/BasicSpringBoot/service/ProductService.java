package com.jerrycf.BasicSpringBoot.service;


import com.jerrycf.BasicSpringBoot.errors.ProductNotFoundException;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*** GET ***/
    public List<Product> listAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> listCheaper(Double limit) {
        return productRepository.findByPriceLessThan(limit);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /*** POST ***/
    public Product create(Product product) {
        return productRepository.save(product);
    }

    /*** DELETE ***/
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
