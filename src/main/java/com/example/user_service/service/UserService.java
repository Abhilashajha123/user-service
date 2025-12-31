package com.example.user_service.service;



import com.example.user_service.dto.UserRequest;
import com.example.user_service.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    public UserResponse createuser(UserRequest userRequest);
    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(@PathVariable Long id);
}
