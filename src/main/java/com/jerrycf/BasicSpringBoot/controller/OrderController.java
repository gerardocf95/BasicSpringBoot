package com.jerrycf.BasicSpringBoot.controller;

import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Order;
import com.jerrycf.BasicSpringBoot.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*** GET ***/
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(){
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    /*** POST ***/
    @PostMapping
    public ResponseEntity<OrderResponse> createNewOrder(@Valid @RequestBody CreateOrderRequest order){
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build().toUri();
        return ResponseEntity.created(location).body(orderService.createOrder(order));
    }

    /*** DELETE ***/
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllOrders() {
        orderService.deleteAllOrders();
        return ResponseEntity.noContent().build();
    }


}
