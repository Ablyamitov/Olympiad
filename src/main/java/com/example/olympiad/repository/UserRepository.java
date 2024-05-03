package com.example.olympiad.repository;

import com.example.olympiad.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    void deleteAllBySession(Long session);

    List<User> findAllBySession(Long session);



}
