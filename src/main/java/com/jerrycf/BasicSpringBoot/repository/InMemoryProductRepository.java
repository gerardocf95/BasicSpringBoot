package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final AtomicLong idSequence = new AtomicLong(0);

    private final ConcurrentHashMap<Long, Product> productMap = new ConcurrentHashMap<>();

    public InMemoryProductRepository() {
        save(new Product(null, "Keyboard", 450.0));
        save(new Product(null, "Mouse", 250.0));
        save(new Product(null, "Monitor", 3200.0));
    }

    @Override
    public List<Product> findAll() {
        return productMap.values().stream().toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public Product save(Product product) {
        Long id = idSequence.incrementAndGet();
        product.setId(id);
        productMap.put(id, product);
        return product;
    }


}