package com.example.user_service.entity;


import com.example.user_service.constant.Role;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "users", uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;


    private Role role;
}
