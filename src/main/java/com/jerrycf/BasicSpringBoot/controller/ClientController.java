package com.jerrycf.BasicSpringBoot.controller;

import com.jerrycf.BasicSpringBoot.model.DTOs.CreateClientRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.ClientResponse;
import com.jerrycf.BasicSpringBoot.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /*** GET ***/
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /*** POST ***/
    @PostMapping
    public ResponseEntity<ClientResponse> save(@Valid @RequestBody CreateClientRequest createClientRequest) {
        ClientResponse response = clientService.createClient(createClientRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    /*** POST ***/
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllClients() {
        clientService.deleteAllClients();
        return ResponseEntity.noContent().build();
    }
}
