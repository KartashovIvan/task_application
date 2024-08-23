package ru.kartashov.task_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с коментарием")
public class CommentResponse {
    @Schema(description = "Автор комметария", example = "author@mail.ru")
    private String author;
    @Schema(description = "Текст коментария", example = "Текст")
    private String text;
}
