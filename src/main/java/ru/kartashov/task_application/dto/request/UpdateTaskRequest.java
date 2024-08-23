package ru.kartashov.task_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на редактирование задачи")
public class UpdateTaskRequest {
    @Schema(description = "ID задачи", example = "1")
    @NotBlank(message = "ID не может быть пустыми")
    private String id;
    @Schema(description = "Тема задачи", example = "Текст темы")
    @Size(min = 1, max = 255, message = "Тема задачи должна быть от 1 до 255 символов")
    private String title;
    @Schema(description = "Описание задачи", example = "Нужно сделать задачу")
    private String description;
    @Schema(description = "Приоритет задачи", example = "HIGH")
    @Size(min = 4, max = 50, message = "Приоритеты задачи: HIGH, MIDDLE, LOW")
    private String priority;
    @Schema(description = "Email исполнителя задачи", example = "ivan@mail.ru")
    private String executor;
}
