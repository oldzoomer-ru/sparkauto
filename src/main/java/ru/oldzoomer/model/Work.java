package ru.oldzoomer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "works")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название работы не может быть пустым")
    private String name;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Нормо-часы должны быть больше 0")
    private Double normalHours;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена за час должна быть больше 0")
    private Double pricePerHour;
}
