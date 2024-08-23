package ru.kartashov.task_application.it;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.kartashov.task_application.dto.response.CommentResponse;
import ru.kartashov.task_application.dto.request.NewCommentRequest;
import ru.kartashov.task_application.dto.request.NewTaskRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CommentControllerTest extends TaskApplicationTest {

    @Test
    @DisplayName("Успешное создание коментария к задаче")
    public void createComment() {
        String idTask = createTestTaskForComment();

        NewCommentRequest newComment = new NewCommentRequest(idTask, "test comment");

        RestAssured
                .given(requestSpecification)
                .body(newComment)
                .auth()
                .oauth2(token)
                .when()
                .post("/comment/add")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Успешный вывод коментариев к задаче")
    public void showComment() {
        String idTask = createTestTaskForComment();

        NewCommentRequest newComment = new NewCommentRequest(idTask, "test comment");

        RestAssured
                .given(requestSpecification)
                .body(newComment)
                .auth()
                .oauth2(token)
                .when()
                .post("/comment/add");

        List<CommentResponse> comments = RestAssured
                .given(requestSpecification)
                .auth()
                .oauth2(token)
                .when()
                .get("/comment/show?taskId=" + idTask)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .response()
                .jsonPath()
                .getList("content");

        assertFalse(comments.isEmpty());
    }


    private String createTestTaskForComment() {
        NewTaskRequest newTask = new NewTaskRequest("test",
                "test",
                "LOW",
                null);
        return getIdTaskFromURI(createTestTask(newTask));
    }
}