package ru.kartashov.task_application.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.kartashov.task_application.dto.request.ChangeStatusRequest;
import ru.kartashov.task_application.dto.request.NewTaskRequest;
import ru.kartashov.task_application.dto.response.TaskResponse;
import ru.kartashov.task_application.dto.request.UpdateTaskRequest;
import ru.kartashov.task_application.entity.PriorityTask;
import ru.kartashov.task_application.entity.StatusTask;
import ru.kartashov.task_application.entity.Task;
import ru.kartashov.task_application.entity.User;
import ru.kartashov.task_application.repository.CommentRepository;
import ru.kartashov.task_application.repository.TaskRepository;
import ru.kartashov.task_application.utils.JwtUtils;

import java.util.Iterator;
import java.util.Objects;

import static ru.kartashov.task_application.entity.StatusTask.*;

@Service
@Data
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public Task createTask(NewTaskRequest task) {
        User authorTask = userService.takeUserByEmail(JwtUtils.takeUserEmailFromSession());
        User executorTask = checkExecutorEmail(task.getExecutorEmail());

        return taskRepository.save(Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(defineStatusTask(task))
                .priority(checkPriority(task.getPriority()))
                .author(authorTask)
                .executor(executorTask)
                .build());
    }

    public Slice<TaskResponse> showAllTask(Integer offset, Integer limit) {
        return taskRepository.findAll(PageRequest.of(offset, limit))
                .map(this::parseTaskToResponse);
    }

    public void deleteTask(String id) {
        Task task = takeTaskById(id);
        taskRepository.delete(task);
    }

    //    Редактировать задачу может только автор или пользователь являющийся исполнителем задачи
    public TaskResponse updateTask(UpdateTaskRequest updateRequest) {
        Task task = takeTaskById(updateRequest.getId());
        String emailAuthUser = JwtUtils.takeUserEmailFromSession();

        checkCompletelyStatusTask(task);

//        Проверка на null исполнителя редактируемой задачи
        if (Objects.nonNull(task.getExecutor())) {
//            Проверка кто меняет задачу, это может быть автор или исполнитель
            if (task.getAuthor().getEmail().equals(emailAuthUser) || task.getExecutor().getEmail().equals(emailAuthUser)) {
                editTaskInformation(task, updateRequest);
                return parseTaskToResponse(task);
            }
        } else {
//            Если проверка исполнителя не прошла, то задачу редактировать может только автор
            if (task.getAuthor().getEmail().equals(emailAuthUser)) {
                editTaskInformation(task, updateRequest);
                return parseTaskToResponse(task);
            }
        }
        throw new RuntimeException("User %s can't change task %s, he is not the author or executor"
                .formatted(emailAuthUser, task.getTitle()));
    }

    public Task takeTaskById(String id) {
        return taskRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Not find task with id %s".formatted(id)));
    }

    public TaskResponse takeTask(String taskId) {
        return parseTaskToResponse(takeTaskById(taskId));
    }

    public TaskResponse changeStatusTask(ChangeStatusRequest statusRequest) {
        Task task = takeTaskById(statusRequest.getTaskId());
        StatusTask newStatusTask = valueOf(statusRequest.getStatusTask());
        String authUserEmail = JwtUtils.takeUserEmailFromSession();

//        Пользователь не может установить у задачи статус WAITING
        if (newStatusTask.equals(WAITING)) {
            throw new RuntimeException("User can't change the task status to WAITING");
        }

        checkCompletelyStatusTask(task);

//      Пользователь может поменять статус задачи на WORK только если статус у задачи WAITING
        if (newStatusTask.equals(WORK) && task.getStatus().equals(WAITING)) {
            task.setStatus(newStatusTask);
            task.setExecutor(checkExecutorEmail(authUserEmail));
            taskRepository.flush();
            return parseTaskToResponse(task);
        }

//        Толкьо пользователь являющийся исполнителем может изменить статус WORK на статус COMPLETED
        if (newStatusTask.equals(COMPLETED) && task.getStatus().equals(WORK)
                && task.getExecutor().getEmail().equals(authUserEmail)) {
            task.setStatus(newStatusTask);
            taskRepository.flush();
            return parseTaskToResponse(task);
        }

        throw new RuntimeException("Error changing task status %S to status %s".formatted(task.getStatus(), newStatusTask));
    }

    public TaskResponse takeInWorkTask(String taskId) {
        Task task = takeTaskById(taskId);
        checkCompletelyStatusTask(task);

        if (task.getStatus().equals(WORK)) {
            throw new RuntimeException("Task already at work");
        }

        String authEmailUser = JwtUtils.takeUserEmailFromSession();
        User executor = userService.takeUserByEmail(authEmailUser);
        task.setExecutor(executor);
        taskRepository.flush();

        return parseTaskToResponse(task);
    }

    public Slice<TaskResponse> takeAuthorTasks(String emailAuthor, Integer offset, Integer limit) {
        User user = userService.takeUserByEmail(emailAuthor);
        return taskRepository.findByAuthor(user, PageRequest.of(offset, limit))
                .map(this::parseTaskToResponse);
    }

    public Slice<TaskResponse> takeExecutorTasks(String emailExecutor, Integer offset, Integer limit) {
        User user = userService.takeUserByEmail(emailExecutor);
        return taskRepository.findByExecutor(user, PageRequest.of(offset, limit))
                .map(this::parseTaskToResponse);
    }

    private void editTaskInformation(Task task, UpdateTaskRequest updateRequest) {
        if (validateTitle(updateRequest.getTitle())) {
            task.setTitle(updateRequest.getTitle());
        }

        if (Objects.nonNull(updateRequest.getDescription())) {
            task.setDescription(updateRequest.getDescription());
        }

        if (Objects.nonNull(updateRequest.getPriority())) {
            task.setPriority(PriorityTask.valueOf(updateRequest.getPriority()));
        }

        validateExecutor(task, updateRequest.getExecutor());

        taskRepository.flush();
    }

    private void validateExecutor(Task task, String executor) {
        if (Objects.nonNull(executor)) {
            User user = userService.takeUserByEmail(executor);
            task.setExecutor(user);
            task.setStatus(WORK);
        }
    }

    private boolean validateTitle(String title) {
        if (title == null) {
            return false;
        } else if (title.isBlank()) {
            throw new RuntimeException("Title can't be empty");
        }
        return true;
    }

    private StatusTask defineStatusTask(NewTaskRequest task) {
        if (task.getExecutorEmail() == null || task.getExecutorEmail().isEmpty()) {
            return WAITING;
        }
        return WORK;
    }

    private PriorityTask checkPriority(String priorityTask) {
        if (priorityTask == null || priorityTask.isEmpty()) {
            return PriorityTask.LOW;
        }
        return PriorityTask.valueOf(priorityTask);
    }

    private TaskResponse parseTaskToResponse(Task task) {
        String executorEmail = null;
        if (task.getExecutor() != null) {
            executorEmail = task.getExecutor().getEmail();
        }
        return new TaskResponse(
                task.getId().toString(),
                task.getAuthor().getEmail(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority().toString(),
                task.getStatus().toString(),
                executorEmail);
    }

    private void checkCompletelyStatusTask(Task task) {
        if (task.getStatus().equals(COMPLETED)) {
            throw new RuntimeException("Impossible to change a completed task");
        }
    }

    private User checkExecutorEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return userService.takeUserByEmail(email);
    }
}
