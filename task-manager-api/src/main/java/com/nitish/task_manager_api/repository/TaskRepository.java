package com.nitish.task_manager_api.repository;

import com.nitish.task_manager_api.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("""
            SELECT t FROM Task t
            WHERE t.taskId = :taskId
            """)
    Optional<Task> findTaskByTaskId(UUID taskId);

    @Modifying
    @Query("""
            DELETE from Task t
            WHERE t.taskId = :taskId
            """)
    void deleteTaskByTaskId(UUID taskId);

    @Query("""
            SELECT t FROM Task t
            WHERE t.createdBy.username = :username
            """)
    Page<Task> findAllTaskByUsername(String username, Pageable pageable);
}
