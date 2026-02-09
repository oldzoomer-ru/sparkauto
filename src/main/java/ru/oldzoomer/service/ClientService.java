package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.repository.ClientRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository repository;

    public void saveClient(@Valid Client client) {
        repository.save(client);
    }
    
    public List<Client> getAllClients() {
        return repository.findAll();
    }
    
    public void deleteClient(Long id) {
        repository.deleteById(id);
    }
}