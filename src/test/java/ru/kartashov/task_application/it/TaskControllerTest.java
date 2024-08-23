package ru.kartashov.task_application.it;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.kartashov.task_application.dto.request.ChangeStatusRequest;
import ru.kartashov.task_application.dto.request.NewTaskRequest;
import ru.kartashov.task_application.dto.request.SignUpRequest;
import ru.kartashov.task_application.dto.request.UpdateTaskRequest;
import ru.kartashov.task_application.dto.response.TaskResponse;
import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.kartashov.task_application.entity.StatusTask.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TaskControllerTest extends TaskApplicationTest {

    private static final NewTaskRequest CREATE_TASK = new NewTaskRequest("test1",
            "test1",
            "LOW",
            null);

    @Test
    @DisplayName("Успешное создание задачи без исполнителя")
    public void createTasks() {
        NewTaskRequest newTestTask = new NewTaskRequest("test title",
                "test description",
                "LOW",
                null);

        Response response = createTestTask(newTestTask);
        response
                .then()
                .statusCode(201)
                .log()
                .all();

        TaskResponse task = takeTestTask(getIdTaskFromURI(response));

        assertTrue(Objects.isNull(task.getExecutorEmail()));
    }

    @Test
    @DisplayName("Успешный вывод задач для авторизованного пользователя")
    public void takeTasks() {
        createTestTask(CREATE_TASK);

        List<TaskResponse> tasks = RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token)
                .get("/task/show")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .response()
                .jsonPath()
                .getList("content");

        assertFalse(tasks.isEmpty());
    }

    @Test
    @DisplayName("Успешное удаление задачи")
    public void deleteTask() {
        String idTask = getIdTaskFromURI(createTestTask(CREATE_TASK));

        RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token)
                .delete("/task/delete?id=" + idTask)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .log()
                .all();
    }

    @Test
    @DisplayName("Успешное смена статуса у задачи на WORK")
    public void changeStatusTask_statusTaskWork() {
        NewTaskRequest newTestTask = new NewTaskRequest("change status task test",
                "test description",
                "LOW",
                null);

        String idTask = getIdTaskFromURI(createTestTask(newTestTask));

        ChangeStatusRequest request = new ChangeStatusRequest(idTask, WORK.toString());

        RestAssured
                .given(requestSpecification)
                .body(request)
                .auth()
                .oauth2(token)
                .when()
                .patch("/task/changeStatus")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .log()
                .all();

        TaskResponse taskWithStatusWork = takeTestTask(idTask);

        assertEquals(taskWithStatusWork.getStatus(), WORK.toString());
        assertTrue(Objects.nonNull(taskWithStatusWork.getExecutorEmail()));
    }

    @Test
    @DisplayName("Успешное смена статуса у задачи на COMPLETED")
    public void changeStatusTask_statusTaskCompleted() {
        NewTaskRequest newTestTask = new NewTaskRequest("change status task test",
                "test description",
                "LOW",
                "test@mail.ru");

        String idTask = getIdTaskFromURI(createTestTask(newTestTask));

        ChangeStatusRequest request = new ChangeStatusRequest(idTask, COMPLETED.toString());

        RestAssured
                .given(requestSpecification)
                .body(request)
                .auth()
                .oauth2(token)
                .when()
                .patch("/task/changeStatus")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .log()
                .all();

        TaskResponse taskWithStatusWork = takeTestTask(idTask);

        assertEquals(taskWithStatusWork.getStatus(), COMPLETED.toString());
    }

    @Test
    @DisplayName("Ошибка при смене статуса с WAITING на COMPLETED")
    public void changeStatusTask_statusTaskFromWorkToWaiting() {
        NewTaskRequest newTestTask = new NewTaskRequest("change status task test",
                "test description",
                "LOW",
                null);

        String idTask = getIdTaskFromURI(createTestTask(newTestTask));

        ChangeStatusRequest request = new ChangeStatusRequest(idTask, COMPLETED.toString());

        RestAssured
                .given(requestSpecification)
                .body(request)
                .auth()
                .oauth2(token)
                .when()
                .patch("/task/changeStatus")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .log()
                .all();

        TaskResponse taskWithStatusWork = takeTestTask(idTask);

        assertEquals(taskWithStatusWork.getStatus(), WAITING.toString());
    }

    @Test
    @DisplayName("Успешное получение созданной задачи")
    public void getTask() {
        TaskResponse task = takeTestTask(getIdTaskFromURI(createTestTask(CREATE_TASK)));
        assertEquals(task.getTitle(), CREATE_TASK.getTitle());
        assertEquals(task.getDescription(), CREATE_TASK.getDescription());
        assertEquals(task.getPriority(), CREATE_TASK.getPriority());
        assertEquals(Objects.isNull(task.getExecutorEmail()), Objects.isNull(CREATE_TASK.getExecutorEmail()));
    }

    @Test
    @DisplayName("Успешное обновление задачи")
    public void updateTask() {
        String idCreatedTask = getIdTaskFromURI(createTestTask(CREATE_TASK));

        UpdateTaskRequest updateTaskInfo = new UpdateTaskRequest(idCreatedTask,
                "New title test",
                "New description test",
                "HIGH",
                "test@mail.ru");

        String updateTaskUri = RestAssured
                .given(requestSpecification)
                .body(updateTaskInfo)
                .auth()
                .oauth2(token)
                .when()
                .patch("/task/update")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .extract()
                .as(String.class);


        TaskResponse updateTask = takeTestTask(updateTaskUri.substring(updateTaskUri.indexOf("task/") + 5));

        assertEquals(updateTask.getTitle(), updateTaskInfo.getTitle());
        assertEquals(updateTask.getDescription(), updateTaskInfo.getDescription());
        assertEquals(updateTask.getPriority(), updateTaskInfo.getPriority());
        assertEquals(updateTask.getExecutorEmail(), updateTaskInfo.getExecutor());
    }

    @Test
    @DisplayName("Успешное назначение исполнителя для задачи")
    public void takeOnTask() {
        NewTaskRequest notWorkTask = new NewTaskRequest("Task with status WAITING",
                "Description",
                "LOW",
                null);

        String idTask = getIdTaskFromURI(createTestTask(notWorkTask));
        TaskResponse workTask = RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token)
                .when()
                .patch("/task/work/" + idTask)
                .then()
                .log().all()
                .statusCode(HttpStatus.ACCEPTED.value())
                .extract()
                .as(TaskResponse.class);

        assertTrue(Objects.nonNull(workTask.getExecutorEmail()));
    }

    @Test
    @DisplayName("Успешная выборка задач по автору")
    public void takeOnlyAuthorTask() {
        List<NewTaskRequest> tasks = List.of(new NewTaskRequest("task with author",
                        "Description",
                        "LOW",
                        null),
                new NewTaskRequest("task with author two",
                        "Description",
                        "LOW",
                        null));

        String randomText = UUID.randomUUID().toString().replace("-", "");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName(randomText);
        signUpRequest.setEmail(randomText + "@mail.ru");
        signUpRequest.setPassword(randomText);


        JwtResponse token = RestAssured
                .given(requestSpecification)
                .body(signUpRequest)
                .when()
                .post("/auth/create")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(JwtResponse.class);
        createTaskCertainAuthor(token.getToken(), tasks);

        List<TaskResponse> tasksAuthor = RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token.getToken())
                .when()
                .get("/task/author/%s@mail.ru".formatted(randomText))
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .jsonPath()
                .getList("content");

        assertEquals(tasksAuthor.size(), 2);
    }

    @Test
    @DisplayName("Успешная выборка задач по исполнителю")
    public void takeOnlyExecutorTask() {
        String randomText = UUID.randomUUID().toString().replace("-", "");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName(randomText);
        signUpRequest.setEmail(randomText + "@mail.ru");
        signUpRequest.setPassword(randomText);

        List<NewTaskRequest> tasks = List.of(new NewTaskRequest("task with executor",
                        "Description",
                        "LOW",
                        randomText + "@mail.ru"),
                new NewTaskRequest("task with executor two",
                        "Description",
                        "LOW",
                        randomText + "@mail.ru"));

        JwtResponse token = RestAssured
                .given(requestSpecification)
                .body(signUpRequest)
                .when()
                .post("/auth/create")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(JwtResponse.class);
        createTaskCertainAuthor(token.getToken(), tasks);

        List<TaskResponse> tasksAuthor = RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token.getToken())
                .when()
                .get("/task/executor/%s@mail.ru".formatted(randomText))
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .jsonPath()
                .getList("content");

        assertEquals(tasksAuthor.size(), 2);
    }

    private void createTaskCertainAuthor(String tokenAuthor, List<NewTaskRequest> tasks) {
        tasks.forEach(task -> {
            RestAssured
                    .given(requestSpecification)
                    .body(task)
                    .auth()
                    .oauth2(tokenAuthor)
                    .when()
                    .post("/task/create");
        });
    }
}