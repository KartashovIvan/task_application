package ru.kartashov.task_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kartashov.task_application.dto.request.SignUpRequest;
import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import ru.kartashov.task_application.dto.request.SignInRequest;
import ru.kartashov.task_application.entity.User;
import ru.kartashov.task_application.repository.UserRepository;
import ru.kartashov.task_application.security.JwtService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public JwtResponse create(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email %s already exist".formatted(request.getEmail()));
        }

        if (userRepository.existsByName(request.getName())) {
            throw new RuntimeException("User with name %s already exist".formatted(request.getName()));
        }

        if (Objects.isNull(request.getPassword()) || request.getPassword().isBlank()) {
            throw new RuntimeException("Password can't be empty");
        }

        User user = parseDtoToUser(request);
        save(user);
        return jwtService.generateAuthToken(user.getEmail());
    }

    public User takeUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email %s not registrant".formatted(email)));
    }

    public JwtResponse singIn(SignInRequest userCredentialsDTO) {
        User user = findByCredentials(userCredentialsDTO);
        return jwtService.generateAuthToken(user.getEmail());
    }

    private User parseDtoToUser(SignUpRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();
    }

    private User findByCredentials(SignInRequest userCredentialsDTO) {
        User existUser = userRepository.findByEmail(userCredentialsDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email %s not registrant".formatted(userCredentialsDTO.getEmail())));

        if (passwordEncoder.matches(userCredentialsDTO.getPassword(), existUser.getPassword())) {
            return existUser;
        }
        throw new RuntimeException("Email or password is not correct");
    }
}
