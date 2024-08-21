package ru.kartashov.task_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequest {
    private String taskId;
    private String statusTask;
}
