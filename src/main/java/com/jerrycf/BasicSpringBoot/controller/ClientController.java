package com.jerrycf.BasicSpringBoot.controller;

import com.jerrycf.BasicSpringBoot.model.DTOs.ClientResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Client;
import com.jerrycf.BasicSpringBoot.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /*** GET ***/
    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /*** POST ***/
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }
}
