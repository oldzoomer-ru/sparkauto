package ru.oldzoomer.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.oldzoomer.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull Long> {
}
