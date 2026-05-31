package com.nitish.task_manager_api.service;

import com.nitish.task_manager_api.model.dto.request.UserLoginRequest;
import com.nitish.task_manager_api.model.dto.request.UserRegisterRequest;
import com.nitish.task_manager_api.model.dto.response.UserLoginResponse;

public interface UserService {

    void register(UserRegisterRequest request);

    UserLoginResponse login(UserLoginRequest request);
}
