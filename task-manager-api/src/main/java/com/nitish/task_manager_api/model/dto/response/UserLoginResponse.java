package com.nitish.task_manager_api.model.dto.response;

public record UserLoginResponse(
        String token,
        UserResponse user
) {
}
