package ru.oldzoomer.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.oldzoomer.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull Long> {
    @Query("SELECT o FROM Order o JOIN o.works w WHERE w.id = :workId")
    List<Order> findOrdersByWorkId(@Param("workId") Long workId);
}
