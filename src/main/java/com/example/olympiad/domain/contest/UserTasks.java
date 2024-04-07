package com.example.olympiad.domain.contest;

import com.example.olympiad.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_tasks")
@Data
public class UserTasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session", nullable = false)
    private Long session;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tasknumber", nullable = false)
    private Long taskNumber;

    @Column(name = "filecontent", nullable = false)
    private String fileContent;

    @Column(name = "points")
    private Integer points;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sent_time")
    private ZonedDateTime sentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session", insertable = false, updatable = false)
    @JsonBackReference
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;
}
