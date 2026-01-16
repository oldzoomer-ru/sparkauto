package ru.oldzoomer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.repository.WorkRepository;

@Service
@Validated
@RequiredArgsConstructor
public class WorkService {
    
    private final WorkRepository repository;
    
    @Valid
    public Work saveWork(@Valid Work work) {
        return repository.save(work);
    }
    
    public List<Work> getAllWorks() {
        return repository.findAll();
    }
}