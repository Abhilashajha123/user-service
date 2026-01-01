package com.example.user_service.dto;

import com.example.user_service.constant.Role;
import com.example.user_service.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;


    private UserStatus status;
}
