package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findBySession(Long session);

}
