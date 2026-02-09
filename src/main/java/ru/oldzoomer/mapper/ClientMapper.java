package ru.oldzoomer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.oldzoomer.dto.ClientDTO;
import ru.oldzoomer.model.Client;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDTO(Client client);

    @Mapping(target = "orders", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    List<ClientDTO> toDTOList(List<Client> clients);

    List<Client> toEntityList(List<ClientDTO> clientDTOs);
}
