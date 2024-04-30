package com.example.olympiad.repository;

import com.example.olympiad.domain.contest.UserTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserTasksRepository extends JpaRepository<UserTasks,Long> {
    List<UserTasks> findAllByUserId(Long id);
    List<UserTasks> findAllByUserIdAndTaskNumber(Long userId,Long taskNumber);

    List<UserTasks> findAllBySession(Long session);

    UserTasks findTopByUserIdAndTaskNumberOrderByIdDesc(Long userId,Long taskNumber);
}
