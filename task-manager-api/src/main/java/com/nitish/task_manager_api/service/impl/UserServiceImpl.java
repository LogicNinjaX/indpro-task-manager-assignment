package com.nitish.task_manager_api.service.impl;

import com.nitish.task_manager_api.model.dto.request.UserLoginRequest;
import com.nitish.task_manager_api.model.dto.request.UserRegisterRequest;
import com.nitish.task_manager_api.model.dto.response.UserLoginResponse;
import com.nitish.task_manager_api.model.dto.response.UserResponse;
import com.nitish.task_manager_api.model.entity.User;
import com.nitish.task_manager_api.repository.UserRepository;
import com.nitish.task_manager_api.security.CustomUserDetails;
import com.nitish.task_manager_api.service.UserService;
import com.nitish.task_manager_api.util.JWTUtil;
import com.nitish.task_manager_api.util.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public void register(UserRegisterRequest request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);
        logger.info("User details registered successfully [user id={}, email={}]", user.getUserId(), user.getEmail());
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = authenticationManager.authenticate(authToken);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);
        UserResponse user = userMapper.toUserResponse(userDetails.getUser());
        return new UserLoginResponse(token, user);
    }

}
