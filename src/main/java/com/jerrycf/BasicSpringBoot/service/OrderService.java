package com.jerrycf.BasicSpringBoot.service;

import com.jerrycf.BasicSpringBoot.errors.NotEnoughStockException;
import com.jerrycf.BasicSpringBoot.errors.OrderNotFoundException;
import com.jerrycf.BasicSpringBoot.errors.ProductNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderItemRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderItemResponse;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Order;
import com.jerrycf.BasicSpringBoot.model.entity.OrderItem;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.OrderRepository;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /*** GET ***/
    public List<OrderResponse> getOrders(){
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        return OrderResponse.from(orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found.")));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest order) {
        Order newOrder = new Order();
        newOrder.setClientId(order.clientId());
        newOrder.setDetails(order.details().isEmpty() ? "No details" : order.details());
        double totalPrice = 0.0;

        for (OrderItemRequest item: order.orderItems()){
            if (productRepository.existsById(item.productId())){
                Product currentProduct = productRepository.findById(item.productId()).get();
                if (currentProduct.getStock() >= item.quantity()) {
                    totalPrice += currentProduct.getPrice() * item.quantity();
                    currentProduct.setStock(currentProduct.getStock() - item.quantity());
                    OrderItem newOrderItem = new OrderItem();
                    newOrderItem.setOrder(newOrder);
                    newOrderItem.setProduct(currentProduct);
                    newOrderItem.setQuantity(item.quantity());
                    newOrderItem.setUnitPrice(currentProduct.getPrice());
                    newOrder.getOrderItems().add(newOrderItem);
                } else {
                    throw new NotEnoughStockException("Not enough stock for product: " + currentProduct.getName());
                }
            } else {
                throw new ProductNotFoundException(item.productId());
            }
        }
        newOrder.setTotalPrice(totalPrice);
        newOrder.setCreatedAt(LocalDateTime.now());

        return OrderResponse.from(orderRepository.save(newOrder));

    }


    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }


}
