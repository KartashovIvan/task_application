package ru.kartashov.task_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.kartashov.task_application.dto.CommentResponse;
import ru.kartashov.task_application.dto.NewCommentRequest;
import ru.kartashov.task_application.entity.Comment;
import ru.kartashov.task_application.entity.Task;
import ru.kartashov.task_application.entity.User;
import ru.kartashov.task_application.repository.CommentRepository;
import ru.kartashov.task_application.utils.JwtUtils;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    public void addComment(NewCommentRequest comment) {
        Task task = taskService.takeTaskById(comment.getId());

        User user = userService.takeUserByEmail(JwtUtils.takeUserEmailFromSession());

        commentRepository.save(Comment.builder()
                .author(user)
                .text(comment.getText())
                .task(task).build());
    }

    public Slice<CommentResponse> showAllComments(String id, Integer offset, Integer limit) {
        Task task = taskService.takeTaskById(id);

        return commentRepository.findAllByTask(task, PageRequest.of(offset, limit))
                .map(comment -> new CommentResponse(comment.getAuthor().getEmail(), comment.getText()));
    }
}
