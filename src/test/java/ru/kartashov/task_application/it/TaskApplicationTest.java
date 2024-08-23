package ru.kartashov.task_application.it;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.kartashov.task_application.dto.request.NewTaskRequest;
import ru.kartashov.task_application.dto.response.TaskResponse;
import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import ru.kartashov.task_application.dto.request.SignInRequest;

public class TaskApplicationTest {
    RequestSpecification requestSpecification;
    String token;

    @BeforeEach
    @DisplayName("Получение действительного токена для тестов")
    public void GetValidToken() {
        requestSpecification = RestAssured.given()
                .baseUri("http://localhost")
                .port(8004)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

        SignInRequest credentials = new SignInRequest();
        credentials.setEmail("test@mail.ru");
        credentials.setPassword("pass1");

        token = RestAssured
                .given(requestSpecification)
                .body(credentials)
                .contentType(ContentType.JSON)
                .post("/auth/sing-in")
                .then()
                .extract()
                .body()
                .as(JwtResponse.class)
                .getToken();
    }

    Response createTestTask(NewTaskRequest newTask) {
        return RestAssured
                .given(requestSpecification)
                .body(newTask)
                .auth()
                .oauth2(token)
                .when()
                .post("/task/create");
    }

    String getIdTaskFromURI(Response response) {
        String path = response.then()
                .extract()
                .as(String.class);

        return path.substring(path.indexOf("task/") + 5);
    }

    TaskResponse takeTestTask(String id) {
        return RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token)
                .get("/task/" + id)
                .then()
                .extract()
                .as(TaskResponse.class);
    }
}
