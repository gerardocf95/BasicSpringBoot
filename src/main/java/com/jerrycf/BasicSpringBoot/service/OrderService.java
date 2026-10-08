package com.jerrycf.BasicSpringBoot.service;

import com.jerrycf.BasicSpringBoot.errors.NotEnoughStockException;
import com.jerrycf.BasicSpringBoot.errors.OrderNotFoundException;
import com.jerrycf.BasicSpringBoot.errors.ProductNotFoundException;
import com.jerrycf.BasicSpringBoot.errors.ResourceNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderItemRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Order;
import com.jerrycf.BasicSpringBoot.model.entity.OrderItem;
import com.jerrycf.BasicSpringBoot.model.entity.Product;
import com.jerrycf.BasicSpringBoot.repository.ClientRepository;
import com.jerrycf.BasicSpringBoot.repository.OrderRepository;
import com.jerrycf.BasicSpringBoot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    /*** GET ***/
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(){
        return orderRepository.findAllWithItems().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        return OrderResponse.from(orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new OrderNotFoundException(id)));
    }

    /*** POST ***/
    /***
     * Retryable and tests on OrderServiceConcurrencyTest.java
     *
     * @param order
     * @return OrderResponse
     */
    @Retryable(
            includes = OptimisticLockingFailureException.class,
            maxRetries = 2,
            delay = 50,
            multiplier = 2,
            jitter = 20,
            maxDelay = 500
    )
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest order) {
        Order newOrder = new Order();
        newOrder.setClient(clientRepository.findById(order.clientId())
                .orElseThrow(() ->new ResourceNotFoundException("Client with id: " + order.clientId() + " not found.")));

        newOrder.setDetails(order.details() == null || order.details().isBlank() ? "No details" : order.details());
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest item: order.orderItems()){
            Product currentProduct = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));
            int updated = productRepository.decrementStock(item.productId(), item.quantity());
            if (updated == 0) {
                throw new NotEnoughStockException("Not enough stock for " + currentProduct.getId() + ": " + currentProduct.getName());
            }

            totalPrice = totalPrice.add(currentProduct.getPrice().multiply(BigDecimal.valueOf(item.quantity())));

            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrder(newOrder);
            newOrderItem.setProduct(currentProduct);
            newOrderItem.setQuantity(item.quantity());
            newOrderItem.setUnitPrice(currentProduct.getPrice());
            newOrder.getOrderItems().add(newOrderItem);

        }
        newOrder.setTotalPrice(totalPrice);

        return OrderResponse.from(orderRepository.save(newOrder));

    }


    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }


}
