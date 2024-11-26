package ru.ivanems.task.dto;

import ru.ivanems.task.entity.util.TaskType;

public class TaskDTO {

    private Long id, userId;
    private String title, description;
    private TaskType taskType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskDTO(Long id, Long userId, String title, String description, TaskType taskType) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
    }

    public TaskDTO() {}
}
