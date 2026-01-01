package com.example.user_service.dto;

import com.example.user_service.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Name cannot be null or empty")
    private String name;

    @NotBlank(message = "Email cannot be null or empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be null or empty")
    private String password;


    private Role role;
}
