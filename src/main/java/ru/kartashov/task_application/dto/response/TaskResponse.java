package ru.kartashov.task_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    @Schema(description = "ID задачи", example = "1")
    private String id;
    @Schema(description = "Email автора задачи", example = "ivan@mail.ru")
    private String authorEmail;
    @Schema(description = "Тема задачи", example = "Текст темы")
    private String title;
    @Schema(description = "Описание задачи", example = "Нужно сделать задачу")
    private String description;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private String priority;
    @Schema(description = "Статус задачи", example = "WORK")
    private String status;
    @Schema(description = "Email исполнителя задачи", example = "ivan@mail.ru")
    private String executorEmail;
}
