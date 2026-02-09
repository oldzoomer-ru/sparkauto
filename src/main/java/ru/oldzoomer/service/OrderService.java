package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.model.Order;
import ru.oldzoomer.repository.OrderRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;

    public void saveOrder(@Valid Order order) {
        orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}