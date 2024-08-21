package ru.kartashov.task_application.dto.secret;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
