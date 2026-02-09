package ru.oldzoomer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.oldzoomer.dto.OrderDTO;
import ru.oldzoomer.model.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WorkMapper.class, ClientMapper.class})
public interface OrderMapper {
    @Mapping(target = "totalHours", expression = "java(order.getTotalHours())")
    @Mapping(target = "totalPrice", expression = "java(order.getTotalPrice())")
    OrderDTO toDTO(Order order);

    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOList(List<Order> orders);

    List<Order> toEntityList(List<OrderDTO> orderDTOs);
}