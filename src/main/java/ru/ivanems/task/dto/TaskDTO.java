package ru.ivanems.task.dto;

public class TaskDTO {

    private Long id, userId;
    private String title, description;

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

    public TaskDTO(Long id, String title, String description, Long userId) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    public TaskDTO() {}
}
