package com.example.user_service.serviceImpl;


import com.example.user_service.Repository.UserRepo;
import com.example.user_service.dto.UserRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.IdNotFoundException;
import com.example.user_service.exception.UserCreationException;
import com.example.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
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

            // Save
            User savedUser = userRepo.save(user);

            // Entity → Response DTO
            return new UserResponse(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail()
            );

        } catch (Exception ex) {
            log.error("Error while creating user", ex);
            throw new UserCreationException("Unable to create user");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers =  userRepo.findAll();

        return allUsers.stream().map(user->new UserResponse(user.getId(),user.getName(),user.getEmail()))
                .collect(Collectors.toList());

    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new IdNotFoundException("User not found"));

        return new UserResponse(user.getId(),user.getName(),user.getEmail());
    }
}
