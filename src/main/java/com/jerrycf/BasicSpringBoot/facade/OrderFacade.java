package com.jerrycf.BasicSpringBoot.facade;


import com.jerrycf.BasicSpringBoot.model.DTOs.CreateOrderRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.OrderResponse;
import com.jerrycf.BasicSpringBoot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;

    @Retryable(includes = OptimisticLockingFailureException.class,
            maxRetries = 3, delay = 50, multiplier = 2, jitter = 20)
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        return orderService.createOrder(createOrderRequest);
    }
}
