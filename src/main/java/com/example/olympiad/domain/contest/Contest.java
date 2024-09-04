package com.example.olympiad.domain.contest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "contest")
@Data
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session")
    private Long session;

    @Column(name = "name")
    private String name;

    @Column(name = "participant_count")
    private int participantCount;

    @Column(name = "judge_count")
    private int judgeCount;

    @Column(name = "username_prefix")
    private String usernamePrefix;

    @Column(name = "duration")
    private String duration;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Tasks> tasks;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ContestState state;

}
