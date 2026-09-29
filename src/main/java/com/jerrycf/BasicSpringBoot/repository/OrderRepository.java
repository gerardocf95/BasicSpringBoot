package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems i " +
            "LEFT JOIN FETCH i.product")
    List<Order> findAllWithItems();
}
