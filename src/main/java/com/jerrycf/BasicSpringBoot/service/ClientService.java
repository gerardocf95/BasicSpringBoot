package com.jerrycf.BasicSpringBoot.service;


import com.jerrycf.BasicSpringBoot.errors.EmailAlreadyRegisteredException;
import com.jerrycf.BasicSpringBoot.errors.ResourceNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.CreateClientRequest;
import com.jerrycf.BasicSpringBoot.model.DTOs.ClientResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Client;
import com.jerrycf.BasicSpringBoot.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /*** GET ***/

    public List<ClientResponse> getClients(){
        return clientRepository.findAll().stream()
                .map(ClientResponse::from)
                .toList();
    }

    public ClientResponse getClientById(Long id) {
        return clientRepository.findById(id).map(ClientResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
    }

    /*** POST ***/
    @Transactional
    public ClientResponse createClient(CreateClientRequest request){
        if (clientRepository.findClientByEmail(request.email()) != null){
            throw new EmailAlreadyRegisteredException("Client with email " + request.email() + " is already registered");
        }

        Client client = new Client();
        client.setEmail(request.email());
        // TODO hash password
        client.setPassword(request.password());
        client.setName(request.name());
        client.setAge(request.age());

        return ClientResponse.from(clientRepository.save(client));
    }


}
