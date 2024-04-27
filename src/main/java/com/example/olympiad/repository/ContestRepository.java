package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findBySession(Long session);
    @Query("SELECT c FROM Contest c WHERE c.session = (SELECT MAX(c2.session) FROM Contest c2)")
    Optional<Contest> findContestWithMaxSession();
}
