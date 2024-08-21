package ru.kartashov.task_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> createdTasks = new ArrayList<>();
    @OneToMany(mappedBy = "executor", cascade = CascadeType.ALL)
    private List<Task> executeTasks = new ArrayList<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}

