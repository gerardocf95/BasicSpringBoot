package com.jerrycf.BasicSpringBoot.repository;

import com.jerrycf.BasicSpringBoot.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
