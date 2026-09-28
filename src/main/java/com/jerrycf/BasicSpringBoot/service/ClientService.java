package com.jerrycf.BasicSpringBoot.service;


import com.jerrycf.BasicSpringBoot.errors.EmailAlreadyRegisteredException;
import com.jerrycf.BasicSpringBoot.errors.ResourceNotFoundException;
import com.jerrycf.BasicSpringBoot.model.DTOs.ClientResponse;
import com.jerrycf.BasicSpringBoot.model.entity.Client;
import com.jerrycf.BasicSpringBoot.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /*** GET ***/

    public List<Client> getClients(){
        return clientRepository.findAll();
    }

    public ClientResponse getClientById(Long id) {
        return clientRepository.findById(id).map(ClientResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
    }

    /*** POST ***/
    public ClientResponse createClient(Client request){
        if (clientRepository.findClientByEmail(request.getEmail()) != null){
            throw new EmailAlreadyRegisteredException("Client with email " + request.getEmail() + " is already registered");
        }

        return ClientResponse.from(clientRepository.save(request));
    }


}
