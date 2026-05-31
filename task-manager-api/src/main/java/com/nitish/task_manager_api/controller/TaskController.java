package com.nitish.task_manager_api.controller;

import com.nitish.task_manager_api.model.dto.request.TaskCreateRequest;
import com.nitish.task_manager_api.model.dto.request.TaskUpdateRequest;
import com.nitish.task_manager_api.model.dto.response.ApiResponse;
import com.nitish.task_manager_api.model.dto.response.PageResponse;
import com.nitish.task_manager_api.model.dto.response.TaskResponse;
import com.nitish.task_manager_api.security.CustomUserDetails;
import com.nitish.task_manager_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(path = "/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody TaskCreateRequest request){
        var response = taskService.createTask(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Task created successfully"));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getAllTask(@AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable){
        var response = taskService.getAllTask(userDetails.getUsername(), pageable);

        return ResponseEntity.ok(ApiResponse.success(response, "Task details fetched successfully"));
    }

    @PatchMapping(path = "/{taskId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskDetails(@PathVariable UUID taskId, @RequestBody TaskUpdateRequest updateRequest){
        var response = taskService.updateTask(taskId, updateRequest);

        return ResponseEntity.ok(ApiResponse.success(response, "Task details updated successfully"));
    }

    @DeleteMapping(path = "/{taskId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable UUID taskId){
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok(ApiResponse.success("Task details deleted successfully"));
    }
}
