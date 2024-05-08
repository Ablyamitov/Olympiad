package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.ContestState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findBySession(Long session);

    @Query("SELECT c FROM Contest c WHERE c.session = (SELECT MAX(c2.session) FROM Contest c2)")
    Optional<Contest> findContestWithMaxSession();

    Page<Contest> findByNameContainingAndStateIn(String name, List<ContestState> states, Pageable pageable);
}
