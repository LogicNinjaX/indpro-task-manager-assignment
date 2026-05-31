package com.nitish.task_manager_api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest
        (
                @NotBlank(message = "Task title is required")
                @Size(min = 3, max = 100, message = "Task title must be between 3 and 100 characters")
                String taskTitle,

                @Size(max = 500, message = "Description cannot exceed 500 characters")
                String description
        ) { }
