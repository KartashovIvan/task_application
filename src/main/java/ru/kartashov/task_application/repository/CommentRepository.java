package ru.kartashov.task_application.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kartashov.task_application.entity.Comment;
import ru.kartashov.task_application.entity.Task;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByTask(Task task, Pageable pageable);
}
