package com.example.user_service.serviceImpl;


import com.example.user_service.Repository.UserRepo;
import com.example.user_service.constant.Role;
import com.example.user_service.constant.UserStatus;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.UserRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.IdNotFoundException;
import com.example.user_service.exception.UserCreationException;
import com.example.user_service.security.JwtUtil;
import com.example.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse createuser(UserRequest userRequest) {

        if(userRepo.existsByEmail((userRequest.getEmail()))){
            throw new UserCreationException("Email already exists");
        }
        try {
            // DTO → Entity
            User user = new User();
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());

            user.setRole(Role.USER);
            user.setStatus(UserStatus.ACTIVE);
            // Save
            User savedUser = userRepo.save(user);

            // Entity → Response DTO
            return new UserResponse(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getRole(),
                    savedUser.getStatus()
            );

        } catch (Exception ex) {
            log.error("Error while creating user", ex);
            throw new UserCreationException("Unable to create user");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers =  userRepo.findAll();

        return allUsers.stream().map(user->new UserResponse(user.getId(),user.getName(),
                        user.getEmail(), user.getRole(),user.getStatus())).collect(Collectors.toList());

    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new IdNotFoundException("User not found"));

        return new UserResponse(user.getId(),user.getName(),user.getEmail(),user.getRole(),user.getStatus());
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail());
        if(user == null){
            throw new UserCreationException("Invalid user");
        }
        if(!user.getPassword().equals(request.getPassword())){
            throw new UserCreationException("Invalid password");
        }
        String token = jwtUtil.generateToken(request.getEmail(),user.getRole());
        return new LoginResponse(token);

    }

    @Override
    public UserResponse deactivate(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new IdNotFoundException("User not found"));

        user.setStatus(UserStatus.DEACTIVATED);
        User deactivatedUser = userRepo.save(user);

        return mapToResponse(deactivatedUser);
    }

    private UserResponse mapToResponse(User user) {

        return Optional.ofNullable(user)
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getStatus()
                ))
                .orElseThrow(() -> new RuntimeException("User is null"));
    }
}
