package com.nitish.task_manager_api.model.dto.response;

import com.nitish.task_manager_api.model.entity.Stage;

import java.util.UUID;

public record TaskResponse
        (
                UUID taskId,
                String taskTitle,
                String description,
                Stage stage
        ) {
}
