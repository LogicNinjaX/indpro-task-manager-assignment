package com.nitish.task_manager_api.service;

import com.nitish.task_manager_api.model.dto.request.TaskCreateRequest;
import com.nitish.task_manager_api.model.dto.request.TaskUpdateRequest;
import com.nitish.task_manager_api.model.dto.response.PageResponse;
import com.nitish.task_manager_api.model.dto.response.TaskResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    TaskResponse createTask(String username, TaskCreateRequest request);

    PageResponse<TaskResponse> getAllTask(String username, Pageable pageable);

    TaskResponse updateTask(UUID taskId, TaskUpdateRequest updateRequest);

    void deleteTaskById(UUID taskId);
}
