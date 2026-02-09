package ru.oldzoomer.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.oldzoomer.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<@NonNull Client, @NonNull Long> {
}
