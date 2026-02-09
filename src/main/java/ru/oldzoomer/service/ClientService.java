package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.dto.ClientDTO;
import ru.oldzoomer.mapper.ClientMapper;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.repository.ClientRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository repository;
    private final ClientMapper clientMapper;

    public void saveClient(@Valid ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        repository.save(client);
    }

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDTOList(repository.findAll());
    }
    
    public void deleteClient(Long id) {
        repository.deleteById(id);
    }
}