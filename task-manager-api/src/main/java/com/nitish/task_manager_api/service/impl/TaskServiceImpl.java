package com.nitish.task_manager_api.service.impl;

import com.nitish.task_manager_api.exception.TaskNotFoundException;
import com.nitish.task_manager_api.exception.UserNotFoundException;
import com.nitish.task_manager_api.model.dto.request.TaskCreateRequest;
import com.nitish.task_manager_api.model.dto.request.TaskUpdateRequest;
import com.nitish.task_manager_api.model.dto.response.PageResponse;
import com.nitish.task_manager_api.model.dto.response.TaskResponse;
import com.nitish.task_manager_api.model.entity.Stage;
import com.nitish.task_manager_api.model.entity.Task;
import com.nitish.task_manager_api.model.entity.User;
import com.nitish.task_manager_api.repository.TaskRepository;
import com.nitish.task_manager_api.repository.UserRepository;
import com.nitish.task_manager_api.service.TaskService;
import com.nitish.task_manager_api.util.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }


    @Override
    public TaskResponse createTask(String username, TaskCreateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Task task = taskMapper.toTask(request);
        task.setStage(Stage.TODO);
        task.setCreatedBy(user);

        task = taskRepository.save(task);
        logger.info("New Task created successfully [task id={}, username={}]", task.getTaskId(), username);
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public PageResponse<TaskResponse> getAllTask(String username, Pageable pageable) {

        Page<TaskResponse> taskResponsePage = taskRepository.findAllTaskByUsername(username, pageable)
                .map(taskMapper::toTaskResponse);

        return PageResponse.from(taskResponsePage);
    }

    @Transactional
    @Override
    public TaskResponse updateTask(UUID taskId, TaskUpdateRequest updateRequest) {
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + taskId));

        if (Objects.nonNull(updateRequest.taskTitle())) {
            task.setTaskTitle(updateRequest.taskTitle());
        }

        if (Objects.nonNull(updateRequest.description())) {
            task.setDescription(updateRequest.description());
        }

        if (Objects.nonNull(updateRequest.stage())) {
            task.setStage(updateRequest.stage());
        }

        task = taskRepository.save(task);
        logger.info("Task details updated successfully [task id={}]", taskId);
        return taskMapper.toTaskResponse(task);
    }

    @Transactional
    @Override
    public void deleteTaskById(UUID taskId) {
        taskRepository.deleteTaskByTaskId(taskId);
        logger.info("Task record deleted successfully [task id={}]", taskId);
    }
}
