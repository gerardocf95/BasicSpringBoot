package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClientRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email not valid")
        String email,

        @JsonIgnore
        String password,

        @NotNull
        Integer age
) {
}
