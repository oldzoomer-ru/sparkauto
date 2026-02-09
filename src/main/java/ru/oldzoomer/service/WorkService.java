package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.dto.WorkDTO;
import ru.oldzoomer.mapper.WorkMapper;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.repository.WorkRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class WorkService {
    
    private final WorkRepository repository;
    private final WorkMapper workMapper;

    public void saveWork(@Valid WorkDTO workDTO) {
        Work work = workMapper.toEntity(workDTO);
        repository.save(work);
    }

    public List<WorkDTO> getAllWorks() {
        return workMapper.toDTOList(repository.findAll());
    }
    
    public void deleteWork(Long id) {
        repository.deleteById(id);
    }
}