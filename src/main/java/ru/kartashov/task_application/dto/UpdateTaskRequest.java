package ru.kartashov.task_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
    private String id;
    private String title;
    private String description;
    private String priority;
    private String executor;
}
