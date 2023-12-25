package com.example.olympiad.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


@Data
public class User {

    private Long id;

    private String name;
    private String username;
    private String password;
    private Long session;


    private String passwordConfirmation;


    private Set<Role> roles;




}
/*
@Entity
@Table(name = "users")
@Data
public class User1 {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "session")
    private Long session;

    @Transient
    private String passwordConfirmation;

    @Transient
    private Set<Role> roles;

}
*/
