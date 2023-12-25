package com.example.olympiad.repository;

import com.example.olympiad.domain.user.Role;
import com.example.olympiad.domain.user.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    void update(User user);
    void create(User user);
    void insertUserRole(Long userId, Role role);
    void delete(Long id);
}
