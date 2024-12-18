package com.cfuv.olympus.repository;

import com.cfuv.olympus.domain.contest.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

import java.util.LinkedList;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Optional<Tasks> findByIdAndSession(Long id, Long session);

    Optional<Tasks> findByTaskIdAndSession(Long taskId, Long session);

    LinkedList<Tasks> findAllBySession(Long session);

    Long countBySession(Long session);


    void deleteAllBySession(Long session);

    boolean existsByTaskIdAndSession(Long taskId, Long session);

}

