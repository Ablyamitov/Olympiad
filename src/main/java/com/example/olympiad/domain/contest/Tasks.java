package com.example.olympiad.domain.contest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data

public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session")
    private Long session;

    @Column(name = "task")
    private String task;

    @Column(name = "points")
    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session", referencedColumnName = "session", insertable = false, updatable = false)
    @JsonBackReference
    private Contest contest;


    @Override
    public String toString() {
        return "Tasks{" +
                "id=" + id +
                ", session=" + session +
                ", task='" + task + '\'' +
                ", points=" + points +
                '}';
    }
}
