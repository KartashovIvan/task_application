package ru.kartashov.task_application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kartashov.task_application.dto.request.ChangeStatusRequest;
import ru.kartashov.task_application.dto.request.NewTaskRequest;
import ru.kartashov.task_application.dto.response.TaskResponse;
import ru.kartashov.task_application.dto.request.UpdateTaskRequest;
import ru.kartashov.task_application.entity.Task;
import ru.kartashov.task_application.service.TaskService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Контроллен управления задачами")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create")
    @Operation(summary = "Создание новой задачи")
    public ResponseEntity<URI> createTask(@RequestBody NewTaskRequest task,
                                          UriComponentsBuilder uriComponentsBuilder) {
        Task newTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", newTask.getId())));
    }

    @GetMapping("/show")
    @Operation(summary = "Вывод всех задач")
    public Slice<TaskResponse> showTask(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                        @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return taskService.showAllTask(offset, limit);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удаление задачи")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTask(@RequestParam String id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/changeStatus")
    @Operation(summary = "Смена статуса задачи")
    public ResponseEntity<URI> changeStatusTask(@RequestBody ChangeStatusRequest statusRequest,
                                                UriComponentsBuilder uriComponentsBuilder) {
        TaskResponse task = taskService.changeStatusTask(statusRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", task.getId())));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Просмотр определенной задачи")
    public TaskResponse take(@PathVariable String taskId) {
        return taskService.takeTask(taskId);
    }

    @PatchMapping("/update")
    @Operation(summary = "Редактирование задачи")
    public ResponseEntity<URI> updateTask(@RequestBody UpdateTaskRequest updateRequest,
                                          UriComponentsBuilder uriComponentsBuilder) {
        TaskResponse updateTask = taskService.updateTask(updateRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(uriComponentsBuilder.path("/task/{taskId}")
                        .build(Map.of("taskId", updateTask.getId())));
    }

    @PatchMapping("/work/{taskId}")
    @Operation(summary = "Взять задачу в работу")
    public ResponseEntity<TaskResponse> takeInWork(@PathVariable String taskId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.takeInWorkTask(taskId));
    }

    @GetMapping("/author/{emailAuthor}")
    @Operation(summary = "Найти задачи по автору")
    public Slice<TaskResponse> takeAuthorTask(@PathVariable String emailAuthor,
                                              @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                              @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return taskService.takeAuthorTasks(emailAuthor, offset, limit);
    }

    @GetMapping("/executor/{emailExecutor}")
    @Operation(summary = "Найти задачи по исполнителю")
    public Slice<TaskResponse> takeExecutorTask(@PathVariable String emailExecutor,
                                                @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                                @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(10) Integer limit) {
        return taskService.takeExecutorTasks(emailExecutor, offset, limit);
    }
}
