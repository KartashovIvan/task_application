package ru.kartashov.task_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    private String id;
    private String authorEmail;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String executorEmail;
}
