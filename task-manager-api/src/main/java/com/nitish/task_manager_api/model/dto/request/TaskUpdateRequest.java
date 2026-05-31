package com.nitish.task_manager_api.model.dto.request;

import com.nitish.task_manager_api.model.entity.Stage;

public record TaskUpdateRequest
        (
                String taskTitle,
                String description,
                Stage stage
        ) { }
