package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.dto.WorkDTO;
import ru.oldzoomer.mapper.WorkMapper;
import ru.oldzoomer.model.Order;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.repository.OrderRepository;
import ru.oldzoomer.repository.WorkRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class WorkService {
    
    private final WorkRepository repository;
    private final OrderRepository orderRepository;
    private final WorkMapper workMapper;

    public void saveWork(@Valid WorkDTO workDTO) {
        Work work = workMapper.toEntity(workDTO);
        repository.save(work);
    }

    public List<WorkDTO> getAllWorks() {
        return workMapper.toDTOList(repository.findAll());
    }
    
    public void deleteWork(Long id) {
        // Find all orders that use this work
        Work work = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work not found with id: " + id));

        // Efficiently find all orders that reference this work
        List<Order> ordersWithWork = orderRepository.findOrdersByWorkId(id);
        // Remove the work from each order's works list
        for (Order order : ordersWithWork) {
            order.getWorks().remove(work);
        }

        // Save updated orders to ensure consistency
        orderRepository.saveAll(ordersWithWork);

        // Now delete the work itself
        repository.deleteById(id);
    }
}
