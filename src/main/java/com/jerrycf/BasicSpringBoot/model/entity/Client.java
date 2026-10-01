package com.jerrycf.BasicSpringBoot.model.entity;

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

    private String name;

    @Email(message = "Email not valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 4, message = "La contraseña debe ser al menos 4 caracteres")
    private String password;

    @NotNull
    private Integer age;
}
