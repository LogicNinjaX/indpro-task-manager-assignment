package com.nitish.task_manager_api.controller;

import com.nitish.task_manager_api.model.dto.request.UserLoginRequest;
import com.nitish.task_manager_api.model.dto.request.UserRegisterRequest;
import com.nitish.task_manager_api.model.dto.response.ApiResponse;
import com.nitish.task_manager_api.model.dto.response.UserLoginResponse;
import com.nitish.task_manager_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest request){
        var response  = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(response, "Login successful"));
    }

    @PostMapping(path = "/register", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterRequest request){
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User details saved successfully"));
    }

}
