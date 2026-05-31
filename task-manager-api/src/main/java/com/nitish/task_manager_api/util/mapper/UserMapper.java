package com.nitish.task_manager_api.util.mapper;

import com.nitish.task_manager_api.model.dto.request.UserRegisterRequest;
import com.nitish.task_manager_api.model.dto.response.UserResponse;
import com.nitish.task_manager_api.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse(User user);
}
