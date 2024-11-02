package com.cfuv.olympus.repository;

import com.cfuv.olympus.domain.contest.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Long> {


    void deleteByIdAndSession(Long id, Long session);

    Optional<Tasks> findByIdAndSession(Long id, Long session);

    Optional<Tasks> findByTaskId(Long taskId);

    LinkedList<Tasks> findAllBySession(Long session);

    Long countBySession(Long session);

    Long findMaxIdBySession(Long session);


    Optional<Tasks> findFirstBySessionOrderByIdDesc(Long session);

    void deleteAllBySession(Long session);

}

