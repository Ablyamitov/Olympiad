package com.example.olympiad.service;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.exception.entity.UserNotFoundException;
import com.example.olympiad.domain.user.Role;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.UserRepository;
import com.example.olympiad.web.dto.user.UserInfo.ChangeUserInfoResponse;
import com.example.olympiad.web.dto.user.UserInfo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();


    @Transactional(readOnly = true)
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }

    @Transactional(readOnly = true)
    public User getByUserId(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }


    @Transactional
    public User create(final User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_PARTICIPANT);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    @Transactional
    public ChangeUserInfoResponse changeUserInfo(final UserInfo userInfo) {
        User user = userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(()->new UserNotFoundException("User does not exits"));


        user.setName(userInfo.getName());
        user.setSurname(userInfo.getSurname());
        user.setEmail(userInfo.getEmail());

        userRepository.save(user);

        ChangeUserInfoResponse changeUserInfoResponse = new ChangeUserInfoResponse();
        changeUserInfoResponse.setId(user.getId());
        changeUserInfoResponse.setName(user.getName());
        changeUserInfoResponse.setSurname(user.getSurname());
        changeUserInfoResponse.setUsername(user.getUsername());
        changeUserInfoResponse.setEmail(user.getEmail());
        changeUserInfoResponse.setSession(user.getSession());
        changeUserInfoResponse.setRole(user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(", ")));
        return changeUserInfoResponse;
    }





    @Transactional
    public Map<User, String> createParticipants(int participantCount, String usernamePrefix, Long session) {
        Map<User, String> participants = new HashMap<>();
        for (int i = 1; i <= participantCount; i++) {
            saveParticipant(usernamePrefix, session, participants, i);

        }
        return participants;
    }

    private void saveParticipant(String usernamePrefix, Long session, Map<User, String> participants, int i) {
        User user = new User();
        String password =  generateRandomString();
        user.setSession(session);
        user.setUsername(usernamePrefix + "_" + session + "_" + i);
        user.setPassword(passwordEncoder.encode(password));
//        user.setName(generateRandomString(5)); // генерируем случайное имя
//        user.setSurname(generateRandomString(5));
//        user.setEmail(generateRandomString(5) + "@example.com"); // генерируем случайный email
        user.setName(null);
        user.setSurname(null);
        user.setEmail(null);
        user.setRoles(Set.of(Role.ROLE_PARTICIPANT));
        userRepository.save(user);
        participants.put(user, password);
    }

    @Transactional
    public Map<User, String> createJudges(int judgeCount, String usernamePrefix, Long session) {
        Map<User, String> judges = new HashMap<>();
        for (int i = 1; i <= judgeCount; i++) {
            saveJudge(usernamePrefix, session, judges, i);
        }
        return judges;
    }

    private void saveJudge(String usernamePrefix, Long session, Map<User, String> judges, int i) {
        User user = new User();
        String password =  generateRandomString();
        user.setSession(session);
        user.setUsername(usernamePrefix + "_J_" + session + "_" + i);
        user.setPassword(passwordEncoder.encode(password));

        user.setName(null); // генерируем случайное имя
        user.setSurname(null);
        user.setEmail(null); // генерируем случайный email
        user.setRoles(Set.of(Role.ROLE_JUDGE));
        userRepository.save(user);
        judges.put(user, password);
    }

    @Transactional
    public Map<User, String> createParticipants(int participantCount, String usernamePrefix, Long session, int existingParticipants) {
        Map<User, String> participants = new HashMap<>();
        for (int i = existingParticipants+1; i <= participantCount+existingParticipants; i++) {
            saveParticipant(usernamePrefix, session, participants, i);

        }
        return participants;
    }

    @Transactional
    public Map<User, String> createJudges(int judgeCount, String usernamePrefix, Long session, int ExistingJudge) {
        Map<User, String> judges = new HashMap<>();
        for (int i = ExistingJudge+1; i <= judgeCount+ExistingJudge; i++) {
            saveJudge(usernamePrefix, session, judges, i);


        }
        return judges;
    }

    private String generateRandomString() {
        if (12 < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    public void deleteParticipantsAndJudges(Contest contest) {
        userRepository.deleteAllBySession(contest.getSession());
    }
}
