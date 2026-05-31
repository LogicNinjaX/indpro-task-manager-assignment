package com.nitish.task_manager_api.util.mapper;

import com.nitish.task_manager_api.model.dto.request.TaskCreateRequest;
import com.nitish.task_manager_api.model.dto.response.TaskResponse;
import com.nitish.task_manager_api.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toTaskResponse(Task task);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "stage", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Task toTask(TaskCreateRequest request);
}
