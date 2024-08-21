package ru.kartashov.task_application.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kartashov.task_application.dto.ChangeStatusRequest;
import ru.kartashov.task_application.dto.NewTaskRequest;
import ru.kartashov.task_application.dto.TaskResponse;
import ru.kartashov.task_application.dto.UpdateTaskRequest;
import ru.kartashov.task_application.entity.Task;
import ru.kartashov.task_application.service.TaskService;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<URI> createTask(@RequestBody NewTaskRequest task,
                                          UriComponentsBuilder uriComponentsBuilder) {
        Task newTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", newTask.getId())));
    }

    @GetMapping("/show")
    public Slice<TaskResponse> showTask(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                        @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return taskService.showAllTask(offset, limit);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTask(@RequestParam String title) {
        taskService.deleteTask(title);
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<URI> changeStatusTask(@RequestBody ChangeStatusRequest statusRequest,
                                                UriComponentsBuilder uriComponentsBuilder) {
        TaskResponse task = taskService.changeStatusTask(statusRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", task.getId())));
    }

    @GetMapping("/{taskId}")
    public TaskResponse take(@PathVariable String taskId) {
        return taskService.takeTask(taskId);
    }

    @PatchMapping("/update")
    public ResponseEntity<URI> updateTask(@RequestBody UpdateTaskRequest updateRequest,
                                          UriComponentsBuilder uriComponentsBuilder) {
        TaskResponse updateTask = taskService.updateTask(updateRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", updateTask.getId())));
    }

    @PatchMapping("/work/{taskId}")
    public ResponseEntity<TaskResponse> takeInWork(@PathVariable String taskId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.takeInWorkTask(taskId));
    }
}
