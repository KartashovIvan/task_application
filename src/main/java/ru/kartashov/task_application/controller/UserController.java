package ru.kartashov.task_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.task_application.dto.SignUpRequest;
import ru.kartashov.task_application.dto.secret.JwtAuthenticationResponse;
import ru.kartashov.task_application.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JwtAuthenticationResponse> createUser(@RequestBody SignUpRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }
}
