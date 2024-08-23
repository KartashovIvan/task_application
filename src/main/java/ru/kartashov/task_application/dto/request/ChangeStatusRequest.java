package ru.kartashov.task_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на изменение статуса задачи")
public class ChangeStatusRequest {
    @Schema(description = "ID задачи", example = "1")
    @NotBlank(message = "ID не может быть пустыми")
    private String taskId;

    @Schema(description = "Статус задачи", example = "WAITING")
    @Size(min = 4, max = 50, message = "Статусы задачи могут быть: WAITING, WORK, COMPLETED")
    @NotBlank(message = "Статус задачи не может быть пустыми")
    private String statusTask;
}
