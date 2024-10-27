package com.cfuv.olympus.repository;

import com.cfuv.olympus.domain.contest.UserTasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserTasksRepository extends JpaRepository<UserTasks,Long> {
    List<UserTasks> findAllByUserId(Long id);
    List<UserTasks> findAllByUserIdAndTaskNumberOrderByIdInSession(Long userId,Long taskNumber);

    List<UserTasks> findAllBySessionOrderByIdInSession(Long session);


    Long findMaxIdBySession(Long session);

    Long countBySession(Long session);

    @Query(value = "SELECT * FROM user_tasks ut1 WHERE ut1.id = (SELECT ut2.id FROM user_tasks ut2 WHERE ut2.tasknumber = ut1.tasknumber AND ut2.session = :session AND ut2.user_id = :userId ORDER BY ut2.id DESC LIMIT 1)", nativeQuery = true)
    List<UserTasks> findAllLatestTasksBySessionAndUserId(@Param("session") Long session, @Param("userId") Long userId);

    Optional<UserTasks> findFirstBySessionOrderByIdDesc(Long session);

}
