package ru.oldzoomer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.repository.ClientRepository;

@Service
@Validated
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository repository;
    
    @Valid
    public Client saveClient(@Valid Client client) {
        return repository.save(client);
    }
    
    public List<Client> getAllClients() {
        return repository.findAll();
    }
}