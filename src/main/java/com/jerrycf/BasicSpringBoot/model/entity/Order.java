package com.jerrycf.BasicSpringBoot.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "ClientId is required")
    private Long clientId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,  orphanRemoval = true)
    @NotEmpty(message = "Items required")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Positive
    private Double totalPrice;

    private String details;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
