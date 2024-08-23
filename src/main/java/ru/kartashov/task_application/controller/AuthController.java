package ru.kartashov.task_application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.task_application.dto.request.SignUpRequest;
import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import ru.kartashov.task_application.dto.request.SignInRequest;
import ru.kartashov.task_application.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Контроллен регистрации и аутентификации")
public class AuthController {
    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<JwtResponse> createUser(@RequestBody SignUpRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PostMapping("/sing-in")
    @Operation(summary = "Аутентификация пользователя")
    public ResponseEntity<JwtResponse> singIn(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.singIn(signInRequest));
    }
}

