package ru.ivanems.task.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.ivanems.task.entity.util.TaskType;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

}
