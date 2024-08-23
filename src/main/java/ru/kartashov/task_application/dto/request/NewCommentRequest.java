package ru.kartashov.task_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание комментария к задачи")
public class NewCommentRequest {
    @Schema(description = "ID задачи", example = "1")
    @NotBlank(message = "ID не может быть пустыми")
    private String id;
    @Schema(description = "Текс комментария", example = "Экзистенциальный текст")
    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;
}
