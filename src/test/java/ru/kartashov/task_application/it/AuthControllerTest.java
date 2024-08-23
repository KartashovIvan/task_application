package ru.kartashov.task_application.it;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.kartashov.task_application.dto.request.SignUpRequest;
import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import ru.kartashov.task_application.dto.request.SignInRequest;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest extends TaskApplicationTest{

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void createUserSuccess() {
        String randomText = UUID.randomUUID().toString().replace("-", "");
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName(randomText);
        signUpRequest.setEmail(randomText + "@mail.ru");
        signUpRequest.setPassword(randomText);

        JwtResponse response = RestAssured
                .given(requestSpecification)
                .body(signUpRequest)
                .when()
                .post("/auth/create")
                .then()
                .statusCode(201)
                .log()
                .all()
                .extract()
                .as(JwtResponse.class);

        assertTrue(Objects.nonNull(response.getToken()));
        assertFalse(response.getToken().isBlank());
    }

    @Test
    @DisplayName("Успешный вход пользователя")
    public void createUserSingIn() {
        String randomText = UUID.randomUUID().toString().replace("-", "");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName(randomText);
        signUpRequest.setEmail(randomText + "@mail.ru");
        signUpRequest.setPassword(randomText);

        JwtResponse responseAfterCreate = RestAssured
                .given(requestSpecification)
                .body(signUpRequest)
                .when()
                .post("/auth/create")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(JwtResponse.class);

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(randomText + "@mail.ru");
        signInRequest.setPassword(randomText);

        JwtResponse token = RestAssured
                .given(requestSpecification)
                .body(signInRequest)
                .when()
                .post("/auth/sing-in")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log()
                .all()
                .extract()
                .as(JwtResponse.class);

        assertTrue(Objects.nonNull(token.getToken()));
    }
}