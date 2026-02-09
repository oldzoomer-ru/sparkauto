package ru.oldzoomer.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.oldzoomer.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<@NonNull Work, @NonNull Long> {
}
