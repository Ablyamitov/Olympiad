package com.example.olympiad.repository.impl;

import com.example.olympiad.domain.exception.ResourceMappingException;
import com.example.olympiad.domain.user.Role;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.DataSourceConfig;
import com.example.olympiad.repository.UserRepository;
import com.example.olympiad.repository.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryIml implements UserRepository {
    private final DataSourceConfig dataSourceConfig;
    private final String FIND_BY_ID = """
            SELECT u.id as user_id,
                u.name as user_name,
                u.username as user_username,
                u.password as user_password,
                u.session as user_session,
                ur.role as user_role_role
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
            WHERE u.id = ?
            """;
    private final String FIND_BY_USERNAME = """
            SELECT u.id as user_id,
                u.name as user_name,
                u.username as user_username,
                u.password as user_password,
                u.session as user_session,
                ur.role as user_role_role
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
            WHERE u.username = ?""";

    private final String UPDATE = """
            UPDATE user
            SET name = ?,
                username = ?,
                password = ?
            WHERE id = ?""";

    private final String CREATE = """
            INSERT INTO users (name, username, password, session)
            VALUES (?,?,?)""";

    private final String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?,?)""";

    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?""";

    @Override
    public Optional<User> findById(Long id){
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1,id);
            try (ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while finding user by id.");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1,username);
            try (ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while finding user by username.");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1,user.getName());
            statement.setString(2,user.getUsername());
            statement.setString(3,user.getPassword());

            statement.setLong(4,user.getSession());

            statement.setLong(5,user.getId());
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1,user.getName());
            statement.setString(2,user.getUsername());
            statement.setString(3,user.getPassword());
            statement.setLong(3,user.getSession());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()){
                rs.next();
                user.setId(rs.getLong(1));

            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while creating user.");
        }
    }


    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1,userId);
            statement.setString(2,role.name());
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1,id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }
}

/*
@Repository
public interface UserRepositoryIml implements JpaRepository<Long,User> {

    User findById();
    User findByUsername();
}
*/
