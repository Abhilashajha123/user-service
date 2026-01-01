package com.example.user_service;

import com.example.user_service.Repository.UserRepo;
import com.example.user_service.constant.Role;
import com.example.user_service.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

    private final UserRepo userRepo;

    public UserServiceApplication(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "abhilasha@gmail.com";

        if (!userRepo.existsByEmail(adminEmail)) {

            User admin = new User();
            admin.setName("Abhilasha");
            admin.setEmail(adminEmail);
            admin.setPassword("admin@123");
            admin.setRole(Role.ADMIN);

            userRepo.save(admin);

            System.out.println("✅ Default ADMIN user created");
        }
    }
}
