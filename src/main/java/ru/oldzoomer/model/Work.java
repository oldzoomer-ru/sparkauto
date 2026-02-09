package ru.oldzoomer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "works")
@Getter
@Setter
@ToString
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название работы не может быть пустым")
    private String name;

    @NotNull(message = "Нормочасы не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = false, message = "Нормо-часы должны быть больше 0")
    private Double normalHours;

    @NotNull(message = "Стоимость за час не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена за час должна быть больше 0")
    private Double pricePerHour;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> objectEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != objectEffectiveClass) {
            return false;
        }
        Work work = (Work) o;
        return getId() != null && Objects.equals(getId(), work.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
