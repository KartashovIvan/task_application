package ru.kartashov.task_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kartashov.task_application.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findByTitleIgnoreCase(String title);
}
