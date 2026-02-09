package ru.oldzoomer.mapper;

import org.mapstruct.Mapper;
import ru.oldzoomer.dto.WorkDTO;
import ru.oldzoomer.model.Work;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkMapper {
    WorkDTO toDTO(Work work);

    Work toEntity(WorkDTO workDTO);

    List<WorkDTO> toDTOList(List<Work> works);

    List<Work> toEntityList(List<WorkDTO> workDTOs);
}