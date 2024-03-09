package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findBySession(Long session);
}
