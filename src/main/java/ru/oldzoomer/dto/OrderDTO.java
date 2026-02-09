package ru.oldzoomer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    @NotNull(message = "Клиент не может быть пустым")
    private ClientDTO client;

    @NotNull(message = "Работы не могут быть пустыми")
    private List<WorkDTO> works;

    private double totalHours;

    private double totalPrice;
}