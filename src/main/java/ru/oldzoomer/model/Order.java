package ru.oldzoomer.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Клиент не может быть пустым")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "Работы не могут быть пустыми")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_works",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id"))
    private List<Work> works;

    public double getTotalHours() {
        if (works == null) return 0.0;
        return works.stream().mapToDouble(Work::getNormalHours).sum();
    }

    public double getTotalPrice() {
        if (works == null) return 0.0;
        return works.stream()
                .mapToDouble(w -> w.getNormalHours() * w.getPricePerHour())
                .sum();
    }
}
