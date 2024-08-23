package ru.kartashov.task_application.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kartashov.task_application.entity.Comment;
import ru.kartashov.task_application.entity.Task;
import ru.kartashov.task_application.entity.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findByTitleIgnoreCase(String title);
    Slice<Task> findByAuthor(User user, Pageable pageable);
    Slice<Task> findByExecutor(User user, Pageable pageable);
}
