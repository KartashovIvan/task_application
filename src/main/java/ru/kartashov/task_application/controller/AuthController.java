package ru.kartashov.task_application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kartashov.task_application.dto.secret.JwtAuthenticationResponse;
import ru.kartashov.task_application.dto.secret.RefreshTokenResponse;
import ru.kartashov.task_application.dto.secret.SignInRequest;
import ru.kartashov.task_application.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/sing-in")
    public ResponseEntity<JwtAuthenticationResponse> singIn(@RequestBody SignInRequest userCredentialsDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.singIn(userCredentialsDTO));
    }

    @PostMapping("/refresh")
    public JwtAuthenticationResponse refresh(@RequestBody RefreshTokenResponse refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
