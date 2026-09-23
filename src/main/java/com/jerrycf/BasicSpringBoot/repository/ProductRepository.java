package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceLessThan(Double limit);
}
