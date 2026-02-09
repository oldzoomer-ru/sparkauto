package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.repository.WorkRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class WorkService {
    
    private final WorkRepository repository;

    public void saveWork(@Valid Work work) {
        repository.save(work);
    }
    
    public List<Work> getAllWorks() {
        return repository.findAll();
    }
    
    public void deleteWork(Long id) {
        repository.deleteById(id);
    }
}