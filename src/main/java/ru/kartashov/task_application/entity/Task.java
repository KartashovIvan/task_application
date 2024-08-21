package ru.kartashov.task_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTask status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityTask priority;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author",nullable = false)
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor")
    private User executor;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
