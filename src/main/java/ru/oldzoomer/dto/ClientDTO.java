package ru.oldzoomer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientDTO {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    private String middleName;

    @Pattern(regexp = "^[A-HJ-NP-TV-Z0-9]{17}$", message = "VIN номер должен содержать 17 символов")
    @NotBlank(message = "VIN номер не может быть пустым")
    private String vinNumber;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Номер телефона должен содержать от 10 до 15 цифр")
    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phone;

    @Email(message = "Некорректный формат email")
    private String email;
}