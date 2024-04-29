package com.example.olympiad.domain.contest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "tasks")
@Data

public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session")
    private Long session;

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
