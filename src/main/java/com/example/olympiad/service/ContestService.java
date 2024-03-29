package com.example.olympiad.service;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.ContestRepository;
import com.example.olympiad.repository.TasksRepository;
import com.example.olympiad.web.dto.contest.AllContestsNameSessionResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestResponse;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.CreatedFile;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TasksRepository tasksRepository;

    private final UserService userService;




    @Retryable(retryFor = ContestNotStartedException.class,
            maxAttempts = 11,
            backoff = @Backoff(delay = 5000))
    public Contest getContestOptionalBySession(Long session) {
        Contest contest = contestRepository.findBySession(session)
                .orElseThrow(() -> new EntityNotFoundException("Contest does not exist."));

        if (contest.getStartTime() != null) return contest;
        else throw new ContestNotStartedException("Contest haven't started yet");

    }




    @Transactional
    public ContestResponse create(final ContestRequest contestRequest){

        if (contestRepository.findBySession(contestRequest.getSession()).isPresent()) {
            throw new IllegalStateException("Contest already exists.");
        }
        ContestResponse contestResponse = new ContestResponse();

        Contest contest = new Contest();
        contest.setSession(contestRequest.getSession());
        contest.setName(contestRequest.getName());
        contest.setParticipantCount(contestRequest.getParticipantCount());
        contest.setJudgeCount(contestRequest.getJudgeCount());
        contest.setUsernamePrefix(contestRequest.getUsernamePrefix());
        contest.setDuration(contestRequest.getDuration());


        Map<User, String> participants = userService.createParticipants(contest.getParticipantCount(), contest.getUsernamePrefix(), contest.getSession());
        Map<User, String> judges = userService.createJudges(contest.getJudgeCount(), contest.getUsernamePrefix(), contest.getSession());

        contestRepository.save(contest);

        ArrayList<Tasks> tasks = new ArrayList<>();
        for (String taskString : contestRequest.getProblems()) {
            Tasks task = new Tasks();
            task.setSession(contest.getSession());
            task.setTask(taskString);
            task.setPoints(0); //вот эту штуку убрать и потом убрать not null в бд

            tasks.add(task);
            tasksRepository.save(task);
        }

        contest.setTasks(tasks);
        contestRepository.save(contest);



        File file = createFile(contest, participants, judges);

        contestResponse.setContest(contest);
        contestResponse.setFile(file);
        contestResponse.setTasks(tasks);

        return contestResponse;
    }

    public CreatedFile createUsers(final CreateUsersRequest createUsersRequest) {
        /*if (contestRepository.findBySession(createUsersRequest.getSession()).isPresent()) {
            throw new IllegalStateException("Contest already exists.");
        }*/
        Contest contest = contestRepository.findBySession(createUsersRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));

        Map<User, String> participants = userService.createParticipants(createUsersRequest.getParticipantCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(),contest.getParticipantCount());
        Map<User, String> judges = userService.createJudges(createUsersRequest.getJudgeCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(),contest.getJudgeCount());

        contest.setParticipantCount(contest.getParticipantCount()+ createUsersRequest.getParticipantCount());
        contest.setJudgeCount(contest.getJudgeCount()+ createUsersRequest.getJudgeCount());
        contestRepository.save(contest);

        File file = createFile(contest, participants, judges);

        CreatedFile createdFile = new CreatedFile();
        createdFile.setFile(file);
        return createdFile;
    }

    private File createFile(Contest contest, Map<User, String> participants, Map<User, String> judges) {
        try {
            File file = new File("contest_info.txt");
            FileWriter writer = new FileWriter(file);

            // Записываем информацию об участниках и жюри в файл
            writer.write("Информация об олимпиаде: " + contest.getName() + "\n");
            writer.write("Участники: \n");
            for (Map.Entry<User, String> entry : participants.entrySet()) {
                writer.write("Username: " + entry.getKey().getUsername() + ", Password: " + entry.getValue() + "\n");
            }
            writer.write("Жюри: \n");
            for (Map.Entry<User, String> entry : judges.entrySet()) {
                writer.write("Username: " + entry.getKey().getUsername() + ", Password: " + entry.getValue() + "\n");
            }

            writer.close();
            return file;
        } catch (IOException e) {
            System.err.println("Ошибка с файлом");
        }
        return null;
    }

    public GetStartAndEndContestTimeResponse start(final Long contestSession) {
        Contest contest = contestRepository.findBySession(contestSession)
                .orElseThrow(() ->
                        new ContestNotFoundException("Contest not found."));
        Instant startTime = Instant.now(); // Текущее время
        Instant endTime = startTime.plus(Duration.ofSeconds(contest.getDuration()));
        contest.setStartTime(startTime);
        contest.setEndTime(endTime);
        contestRepository.save(contest);

        GetStartAndEndContestTimeResponse getStartAndEndContestTimeResponse =
                new GetStartAndEndContestTimeResponse();
        getStartAndEndContestTimeResponse.setStartTime(contest.getStartTime());
        getStartAndEndContestTimeResponse.setEndTime(contest.getEndTime());

        return getStartAndEndContestTimeResponse;
    }

    @Transactional
    public void deleteContest(Long contestSession) {
        Contest contest = contestRepository.findBySession(contestSession)
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));
        userService.deleteParticipantsAndJudges(contest);
        contestRepository.delete(contest);
    }

    public List<AllContestsNameSessionResponse> getAllContests() {
        return contestRepository.findAll().stream()
                .distinct()
                .map(contest -> new AllContestsNameSessionResponse(
                        contest.getName(),
                        contest.getSession()))
                .collect(Collectors.toList());
    }

    public Contest getContestBySession(Long session) {
        return contestRepository.findBySession(session)
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));
    }


}
