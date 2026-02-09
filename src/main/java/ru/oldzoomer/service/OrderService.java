package ru.oldzoomer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.dto.OrderDTO;
import ru.oldzoomer.mapper.OrderMapper;
import ru.oldzoomer.model.Order;
import ru.oldzoomer.repository.OrderRepository;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public void saveOrder(@Valid OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOList(orderRepository.findAll());
    }
    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}