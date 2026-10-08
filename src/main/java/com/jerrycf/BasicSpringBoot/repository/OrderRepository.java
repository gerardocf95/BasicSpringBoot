package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems i " +
            "LEFT JOIN FETCH i.product " +
            "LEFT JOIN FETCH o.client")
    List<Order> findAllWithItems();

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems i " +
            "LEFT JOIN FETCH i.product " +
            "LEFT JOIN FETCH o.client " +
            "WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}
