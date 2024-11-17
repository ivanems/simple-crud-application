package ru.ivanems.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ivanems.task.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
