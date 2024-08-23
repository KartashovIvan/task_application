package ru.kartashov.task_application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.task_application.dto.response.CommentResponse;
import ru.kartashov.task_application.dto.request.NewCommentRequest;
import ru.kartashov.task_application.service.CommentService;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@Tag(name = "Контроллер комметариев")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового комментария")
    public void addNewComment(@RequestBody NewCommentRequest comment) {
        commentService.addComment(comment);
    }

    @GetMapping("/show")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Просмотр комментариев к задаче")
    public Slice<CommentResponse> showComment(@RequestParam String taskId,
                                              @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                              @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return commentService.showAllComments(taskId, offset, limit);
    }
}
