package ru.ivanems.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.ivanems.logger.aspect.util.LogBeforeExecution;
import ru.ivanems.logger.aspect.util.LogTimeExecution;
import ru.ivanems.task.dto.TaskDTO;
import ru.ivanems.task.dto.TaskMapper;
import ru.ivanems.task.entity.Task;
import ru.ivanems.task.entity.util.TaskType;
import ru.ivanems.task.kafka.KafkaTaskProducer;
import ru.ivanems.task.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final KafkaTaskProducer producerClient;

    private Task getTask (Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Task with ID " + id + " not found"));
    }

    @LogBeforeExecution
    @LogTimeExecution
    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public TaskDTO getTaskById(Long id) {
        return taskMapper.toDTO(getTask(id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task, "id");
        return taskMapper.toDTO(taskRepository.saveAndFlush(task));
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task taskToUpdate = getTask(id);

        TaskType previousTaskType = taskToUpdate.getTaskType();
        BeanUtils.copyProperties(taskDTO, taskToUpdate, "id");
        taskRepository.saveAndFlush(taskToUpdate);

        if (taskDTO.getTaskType() != previousTaskType)
            producerClient.send(taskDTO);

        return taskMapper.toDTO(taskToUpdate);
    }

    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }

}
