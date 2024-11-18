package ru.ivanems.task.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.ivanems.task.aspect.LogExecutionTime;
import ru.ivanems.task.aspect.LogTaskUpdate;
import ru.ivanems.task.dto.TaskDTO;
import ru.ivanems.task.dto.TaskMapper;
import ru.ivanems.task.entity.Task;
import ru.ivanems.task.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public TaskService(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    private Task getTask (Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Task with ID " + id + " not found"));
    }

    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public TaskDTO getTaskById(Long id) {
        return taskMapper.toDTO(getTask(id));
    }

    @LogTaskUpdate
    @LogExecutionTime
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task, "id");
        return taskMapper.toDTO(taskRepository.saveAndFlush(task));
    }

    @LogTaskUpdate
    @LogExecutionTime
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task taskToUpdate = getTask(id);
        BeanUtils.copyProperties(taskDTO, taskToUpdate, "id");
        return taskMapper.toDTO(taskRepository.saveAndFlush(taskToUpdate));
    }

    @LogExecutionTime
    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }

}
