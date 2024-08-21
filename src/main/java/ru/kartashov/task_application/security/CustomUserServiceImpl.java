package ru.kartashov.task_application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kartashov.task_application.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(CustomUserDetails::new)
                .orElseThrow(() -> new RuntimeException("Not find user with email %s".formatted(email)));

    }
}
