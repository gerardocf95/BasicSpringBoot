package com.jerrycf.BasicSpringBoot.model.DTOs;

import com.jerrycf.BasicSpringBoot.model.entity.Client;

public record ClientResponse(

        String name,
        String email,
        Integer age


) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getName(),
                client.getEmail(),
                client.getAge()
        );
    }
}
