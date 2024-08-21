package ru.kartashov.task_application.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.task_application.dto.CommentResponse;
import ru.kartashov.task_application.dto.NewCommentRequest;
import ru.kartashov.task_application.service.CommentService;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewComment(@RequestBody NewCommentRequest comment) {
        commentService.addComment(comment);
    }

    @GetMapping("/show")
    public Slice<CommentResponse> showComment(@RequestParam String taskId,
                                              @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                              @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return commentService.showAllComments(taskId, offset, limit);
    }
}
