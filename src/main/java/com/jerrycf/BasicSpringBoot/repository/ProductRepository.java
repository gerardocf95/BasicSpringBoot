package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.Model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    public List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);
}
