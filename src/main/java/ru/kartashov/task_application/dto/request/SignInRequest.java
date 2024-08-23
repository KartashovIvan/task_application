package ru.kartashov.task_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на авторизацию")
public class SignInRequest {
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате mail@example.com")
    private String email;
    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 4, message = "Длина пароля должна быть не менее 4 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
