package com.jerrycf.BasicSpringBoot.Model.entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;

    private String name;

    private Double price;
}
