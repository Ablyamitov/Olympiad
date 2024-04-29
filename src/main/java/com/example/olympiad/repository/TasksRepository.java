package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Tasks findBySessionAndName(Long session, String name);

    //List<Tasks> findAllBySession(Long session);

    List<Tasks> getTasksBySession(Long session);


    Tasks findBySessionAndId(Long session, Long id);

    void deleteByIdAndSession(Long id, Long session);

    LinkedList<Tasks> findAllBySession(Long session);


}

