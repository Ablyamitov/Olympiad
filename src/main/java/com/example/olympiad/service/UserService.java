package com.example.olympiad.service;

import com.example.olympiad.domain.user.User;

public interface UserService {
    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);


    //void insertUserRole(Long userId, Role role);

    void delete(Long id);
}
