package ru.kartashov.task_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentRequest {
    private String id;
    private String emailAuthor;
    private String text;
}
