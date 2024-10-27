package com.cfuv.olympus.repository;

import com.cfuv.olympus.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    void deleteAllBySession(Long session);

    List<User> findAllBySession(Long session);
}
