package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final AtomicLong idSequence;

    private final ConcurrentHashMap<Long, Product> productMap = new ConcurrentHashMap<>();

    private InMemoryProductRepository() {
        idSequence = new AtomicLong(0);
        productMap.put(idSequence.get(), new Product(idSequence.getAndIncrement(), "Keyboard", 450.0));
        productMap.put(idSequence.get(), new Product(idSequence.getAndIncrement(), "Mouse", 250.0));
        productMap.put(idSequence.get(), new Product(idSequence.getAndIncrement(), "Monitor", 320.0));
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
        product.setId(idSequence.get());
        productMap.put(idSequence.getAndIncrement(), product);
        return product;
    }


}