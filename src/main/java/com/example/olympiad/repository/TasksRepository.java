package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Tasks findBySessionAndName(Long session, String name);

}

