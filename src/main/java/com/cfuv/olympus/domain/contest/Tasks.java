package com.cfuv.olympus.domain.contest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "session")
    private Long session;

    @Column(name = "html_name")
    private String htmlName;

    @Column(name = "name")
    private String name;

    @Column(name = "task")
    private String task;

    @Column(name = "points")
    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session", referencedColumnName = "session", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore //
    private Contest contest;
}
