package ru.ivanems.task.dto;

import lombok.*;
import ru.ivanems.task.entity.util.TaskType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id, userId;
    private String title, description;
    private TaskType taskType;

}
