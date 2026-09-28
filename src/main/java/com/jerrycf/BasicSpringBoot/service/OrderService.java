package com.jerrycf.BasicSpringBoot.service;

import com.jerrycf.BasicSpringBoot.errors.NotEnoughStockException;
import com.jerrycf.BasicSpringBoot.errors.OrderNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /*** GET ***/
    public ResponseEntity<List<OrderResponse>> getOrders(){
        return ResponseEntity.ok(orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList());
    }

    public ResponseEntity<OrderResponse> getOrderById(Long id) {
        return ResponseEntity.ok(OrderResponse.from(orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found."))));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest order) {
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
        Order newOrder = new Order();
        newOrder.setClientId(order.clientId());
        List<OrderItem> orderItemList = new ArrayList<>();
        order.orderItems().forEach(item -> {
            if (productRepository.findById(item.productId()).isPresent()) {
                // Check quantity on stock
                Product currentProduct = productRepository.findById(item.productId()).get();
                double currentProductPrice = currentProduct.getPrice();

                if (currentProduct.getStock() >= item.quantity()){
                    // update product stock on repository
                    currentProduct.setStock(currentProduct.getStock() - item.quantity());
                    productRepository.save(currentProduct);
                    totalPrice.updateAndGet(v -> v + currentProductPrice * item.quantity());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(newOrder.getId());
                    orderItem.setOrder(newOrder);
                    orderItem.setProduct(currentProduct);
                    orderItem.setQuantity(item.quantity());
                    orderItem.setUnitPrice(currentProductPrice);
                    orderItemList.add(orderItem);
                } else {
                    throw new NotEnoughStockException("Not enough stock for product: " + currentProduct.getName());
                }
            }
        });
        newOrder.setOrderItems(orderItemList);
        newOrder.setTotalPrice(totalPrice.get());
        newOrder.setDetails(order.details().isEmpty() ? "No details" : order.details());
        return OrderResponse.from(orderRepository.save(newOrder));
    }


    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }


}
