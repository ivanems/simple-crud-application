package ru.ivanems.task.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.ivanems.task.aspect.LogExecutionTime;
import ru.ivanems.task.aspect.LogTaskUpdate;
import ru.ivanems.task.entity.Task;
import ru.ivanems.task.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null)
            throw new NullPointerException("Task with ID " + id + " not found");

        return task;
    }

    @LogTaskUpdate
    @LogExecutionTime
    public Task createTask(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @LogTaskUpdate
    @LogExecutionTime
    public Task updateTask(Long id, Task task) {
        Task taskToUpdate = getTaskById(id);

        BeanUtils.copyProperties(task, taskToUpdate, "id");
        return taskRepository.save(taskToUpdate);
    }

    @LogExecutionTime
    public void deleteTask(Long id) {
        Task task = getTaskById(id);

        taskRepository.delete(task);
    }

}
