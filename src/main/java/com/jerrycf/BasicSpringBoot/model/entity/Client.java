package com.jerrycf.BasicSpringBoot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email(message = "Email not valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 4, message = "La contraseña debe ser al menos 4 caracteres")
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer age;
}
