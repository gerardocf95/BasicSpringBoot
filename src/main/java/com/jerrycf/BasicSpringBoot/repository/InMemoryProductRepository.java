package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.Model.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private AtomicLong contId = new AtomicLong(0);

    private final List<Product> products = new ArrayList<>(List.of(
            new Product(contId.incrementAndGet(), "Keyboard", 450.0),
            new Product(contId.incrementAndGet(), "Mouse", 250.0),
            new Product(contId.incrementAndGet(), "Monitor", 3200.0)
    ));

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    @Override
    public Product save(Product product) {
        product.setId(contId.incrementAndGet());
        products.add(product);
        return product;
    }


}