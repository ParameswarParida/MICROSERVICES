package com.example.authservice.repo;

import com.example.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);
}
