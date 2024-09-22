package com.example.olympiad.service;

import com.example.olympiad.domain.exception.entity.user.UserNotFoundException;
import com.example.olympiad.domain.user.Role;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.UserRepository;
import com.example.olympiad.web.dto.user.UserInfo.ChangeUserInfoResponse;
import com.example.olympiad.web.dto.user.UserInfo.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
//
//    @Test
//    void testGetByUsername_UserFound_ReturnsUser() {
//        String username = "testUser";
//        User expectedUser = new User();
//        expectedUser.setUsername(username);
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
//
//        User actualUser = userService.getByUsername(username);
//
//        assertEquals(expectedUser, actualUser);
//    }
//    @Test
//    void testGetByUsername_UserNotFound_ThrowsException() {
//        // Arrange
//        String username = "testUser";
//        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> userService.getByUsername(username));
//    }
//
//    @Test
//    void testGetByUserId_UserFound_ReturnsUser() {
//        Long userId = 1L;
//        User expectedUser = new User();
//        expectedUser.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
//
//        User actualUser = userService.getByUserId(userId);
//
//        assertEquals(expectedUser, actualUser);
//    }
//
//    @Test
//    void testGetByUserId_UserNotFound_ThrowsException() {
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> userService.getByUserId(userId));
//    }
//
//    @Test
//    void testCreateUser_UserDoesNotExist_CreatesAndReturnsUser() {
//        User newUser = new User();
//        newUser.setUsername("testUser");
//        newUser.setPassword("password");
//        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
//        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
//
//        User createdUser = userService.create(newUser);
//
//        assertNotNull(createdUser);
//        assertEquals("encodedPassword", createdUser.getPassword());
//        verify(userRepository, times(1)).save(newUser);
//    }
//    @Test
//    void testChangeUserInfo_UserExists_ChangesInfoAndReturnsResponse() {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername("testUser");
//        userInfo.setName("John");
//        userInfo.setSurname("Doe");
//        userInfo.setEmail("john.doe@example.com");
//
//
//        User existingUser = new User();
//        existingUser.setUsername(userInfo.getUsername());
//        existingUser.setName("OldName");
//        existingUser.setSurname("OldSurname");
//        existingUser.setEmail("old.email@example.com");
//        existingUser.setRoles(Set.of(Role.ROLE_PARTICIPANT));
//
//        when(userRepository.findByUsername(userInfo.getUsername())).thenReturn(Optional.of(existingUser));
//
//        ChangeUserInfoResponse response = userService.changeUserInfo(userInfo);
//
//        assertNotNull(response);
//        assertEquals(existingUser.getId(), response.getId());
//        assertEquals(userInfo.getName(), existingUser.getName());
//        assertEquals(userInfo.getSurname(), existingUser.getSurname());
//        assertEquals(userInfo.getEmail(), existingUser.getEmail());
//    }
//
//    @Test
//    void testCreateParticipants_CreatesParticipantsAndReturnsMap() {
//        int participantCount = 3;
//        String usernamePrefix = "participant";
//        Long session = 1L;
//        Map<User, String> participants = userService.createParticipants(participantCount, usernamePrefix, session);
//
//        assertNotNull(participants);
//        assertEquals(participantCount, participants.size());
//        assertTrue(participants.keySet().stream().allMatch(u -> u.getUsername().startsWith(usernamePrefix)));
//        assertTrue(participants.keySet().stream().allMatch(u -> u.getSession().equals(session)));
//    }
//
//    @Test
//    void testCreateJudges_CreatesJudgesAndReturnsMap() {
//        int judgeCount = 2;
//        String usernamePrefix = "judge";
//        Long session = 1L;
//
//        Map<User, String> judges = userService.createJudges(judgeCount, usernamePrefix, session);
//
//        assertNotNull(judges);
//        assertEquals(judgeCount, judges.size());
//        assertTrue(judges.keySet().stream().allMatch(u -> u.getUsername().startsWith(usernamePrefix)));
//        assertTrue(judges.keySet().stream().allMatch(u -> u.getSession().equals(session)));
//    }

}
