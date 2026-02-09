package ru.oldzoomer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkDTO {
    private Long id;

    @NotBlank(message = "Название работы не может быть пустым")
    private String name;

    @NotNull(message = "Нормо-часы не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = false, message = "Нормо-часы должны быть больше 0")
    private Double normalHours;

    @NotNull(message = "Стоимость за час не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена за час должна быть больше 0")
    private Double pricePerHour;
}