package ru.ivanems.task.dto;

import org.springframework.stereotype.Component;
import ru.ivanems.task.entity.Task;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setUserId(task.getUserId());
        taskDTO.setTaskType(task.getTaskType());
        return taskDTO;
    }

    public Task toEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUserId(taskDTO.getUserId());
        task.setTaskType(taskDTO.getTaskType());
        return task;
    }

}
