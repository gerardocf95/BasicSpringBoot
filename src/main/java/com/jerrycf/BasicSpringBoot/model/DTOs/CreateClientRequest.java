package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClientRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email not valid")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @NotNull
        Integer age
) {
}
