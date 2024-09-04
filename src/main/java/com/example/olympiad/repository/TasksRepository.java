package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TasksRepository extends JpaRepository<Tasks, Long> {


    void deleteByIdAndSession(Long id, Long session);

    LinkedList<Tasks> findAllBySession(Long session);

    Long countBySession(Long session);

    Long findMaxIdBySession(Long session);


    Optional<Tasks> findFirstBySessionOrderByIdDesc(Long session);

}

