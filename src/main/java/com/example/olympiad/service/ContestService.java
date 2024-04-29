package com.example.olympiad.service;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.ContestState;
import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.domain.mail.EmailDetails;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.ContestRepository;
import com.example.olympiad.repository.TasksRepository;
import com.example.olympiad.service.mail.EmailService;
import com.example.olympiad.web.dto.contest.AllContestsNameSessionResponse;
import com.example.olympiad.web.dto.contest.ChangeDuration.ChangeDurationRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ProblemInfo;
import com.example.olympiad.web.dto.contest.EditProblems.AddProblemRequest;
import com.example.olympiad.web.dto.contest.EditProblems.DeleteProblemRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.CreatedFile;
import com.example.olympiad.web.dto.contest.createUsers.FileResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TasksRepository tasksRepository;

    private final UserService userService;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String toAddress;
    public static final Logger log = LoggerFactory.getLogger(ContestService.class);


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
    public ContestAndFileResponse create(final ContestRequest contestRequest) {


        Contest contest = new Contest();

        long maxSession;
        if (contestRepository.findContestWithMaxSession().isPresent())
            maxSession = contestRepository.findContestWithMaxSession().get().getSession() + 1;
        else
            maxSession = 1L;
        contest.setSession(maxSession);
        contest.setName(contestRequest.getName());
        contest.setParticipantCount(contestRequest.getParticipantCount());
        contest.setJudgeCount(contestRequest.getJudgeCount());
        contest.setUsernamePrefix(contestRequest.getUsernamePrefix());
        contest.setDuration(contestRequest.getDuration());
        contest.setState(ContestState.NOT_STARTED);


        Map<User, String> participants = userService.createParticipants(contest.getParticipantCount(), contest.getUsernamePrefix(), contest.getSession());
        Map<User, String> judges = userService.createJudges(contest.getJudgeCount(), contest.getUsernamePrefix(), contest.getSession());

        contestRepository.save(contest);

//        List<Tasks> tasks = new ArrayList<>();
//        for (ProblemInfo problemInfo : contestRequest.getProblemInfos()) {
//            Tasks task = new Tasks();
//            task.setSession(contest.getSession());
//            task.setName(problemInfo.getName());
//            task.setTask(problemInfo.getProblem());
//            task.setPoints(problemInfo.getPoints());
//
//            tasks.add(task);
//            tasksRepository.save(task);
//        }
//
//        contest.setTasks(tasks);
        contest.setTasks(createProblems(contest.getSession(), contestRequest.getProblemInfos()));
        contestRepository.save(contest);

        File file = createFile(contest, participants, judges);
        byte[] fileContent;
        try {
            assert file != null;
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        ContestAndFileResponse response = new ContestAndFileResponse();
        response.setContest(contest);
        response.setFileContent(fileContent);

        if (!file.delete()) log.info("Файл " + file.getName() + " не может быть удален");

        return response;
    }


    public FileResponse createUsers(final CreateUsersRequest createUsersRequest) {
        /*if (contestRepository.findBySession(createUsersRequest.getSession()).isPresent()) {
            throw new IllegalStateException("Contest already exists.");
        }*/
        Contest contest = contestRepository.findBySession(createUsersRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));

        Map<User, String> participants = userService.createParticipants(createUsersRequest.getParticipantCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(), contest.getParticipantCount());
        Map<User, String> judges = userService.createJudges(createUsersRequest.getJudgeCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(), contest.getJudgeCount());

        contest.setParticipantCount(contest.getParticipantCount() + createUsersRequest.getParticipantCount());
        contest.setJudgeCount(contest.getJudgeCount() + createUsersRequest.getJudgeCount());
        contestRepository.save(contest);

        File file = createFile(contest, participants, judges);
        byte[] fileContent;
        try {
            assert file != null;
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFileContent(fileContent);


        if (!file.delete()) log.info("Файл " + file.getName() + " не может быть удален");
        return fileResponse;
    }

    private File createFile(Contest contest, Map<User, String> participants, Map<User, String> judges) {
        Map<User, String> sortedParticipants = participants.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(User::getUsername)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        Map<User, String> sortedJudges = judges.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(User::getUsername)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        try {
            File file = new File("contest_info.txt");
            FileWriter writer = new FileWriter(file);


            StringBuilder body = new StringBuilder();


            // Записываем информацию об участниках и жюри в файл
            writer.write("Информация об олимпиаде: " + contest.getName() + "\n");
            String subject = "Информация об олимпиаде: " + contest.getName();
            writer.write("Участники: \n");
            body.append("Участники: \n");
            for (Map.Entry<User, String> entry : sortedParticipants.entrySet()) {
                writer.write("Username: " + entry.getKey().getUsername() + ", Password: " + entry.getValue() + "\n");
                body.append("Username: ").append(entry.getKey().getUsername()).append(", Password: ").append(entry.getValue()).append("\n");
            }
            writer.write("Жюри: \n");
            for (Map.Entry<User, String> entry : sortedJudges.entrySet()) {
                writer.write("Username: " + entry.getKey().getUsername() + ", Password: " + entry.getValue() + "\n");
                body.append("Username: ").append(entry.getKey().getUsername()).append(", Password: ").append(entry.getValue()).append("\n");
            }

            writer.close();

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setToAddress(toAddress);
            emailDetails.setSubject(subject);
            emailDetails.setBody(String.valueOf(body));

            emailService.sendSimpleMail(emailDetails);

            log.info("Файл " + file.getName() + " создан");
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
        ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC+3")); // Текущее время

        //ZonedDateTime endTime = startTime.plus(Duration.ofSeconds(contest.getDuration()));
        ZonedDateTime endTime = startTime.plus(parseToDuration(contest.getDuration()));
        contest.setStartTime(startTime);
        contest.setEndTime(endTime);
        contest.setState(ContestState.IN_PROGRESS);
        contestRepository.save(contest);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                contest.setState(ContestState.FINISHED);
                contestRepository.save(contest);
            }
        }, Date.from(endTime.toInstant())); // Запуск задачи во время endTime

        GetStartAndEndContestTimeResponse getStartAndEndContestTimeResponse =
                new GetStartAndEndContestTimeResponse();
        getStartAndEndContestTimeResponse.setStartTime(contest.getStartTime());
        getStartAndEndContestTimeResponse.setEndTime(contest.getEndTime());

        return getStartAndEndContestTimeResponse;
    }

    private Duration parseToDuration(String durationStr) {
        String[] durationParts = durationStr.split(":");
        int hours = Integer.parseInt(durationParts[0]);
        int minutes = Integer.parseInt(durationParts[1]);
        return Duration.ofHours(hours).plusMinutes(minutes);
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
                .sorted(Comparator.comparing(Contest::getSession))
                .map(contest -> new AllContestsNameSessionResponse(
                        contest.getName(),
                        contest.getSession(),
                        contest.getState()))
                .collect(Collectors.toList());
    }

    public Contest getContestBySession(Long session) {
        return contestRepository.findBySession(session)
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));
    }


    @Transactional
    public String changeDuration(ChangeDurationRequest changeDurationRequest) {
        Contest contest = contestRepository.findBySession(changeDurationRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));
        contest.setDuration(changeDurationRequest.getNewDuration());
        contestRepository.save(contest);
        return contest.getDuration();
    }

    @Transactional
    public List<Tasks> addProblems(AddProblemRequest addProblemRequest) {
        Contest contest = contestRepository.findBySession(addProblemRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Contest does not exist."));


        Tasks task = new Tasks();
        task.setSession(contest.getSession());
        task.setName(addProblemRequest.getName());
        task.setTask(addProblemRequest.getProblem());
        task.setPoints(addProblemRequest.getPoints());

        tasksRepository.save(task);

        List<Tasks> problems = tasksRepository.findAllBySession(contest.getSession());
        contest.setTasks(problems);
        contestRepository.save(contest);

        return problems;
    }


    private List<Tasks> createProblems(Long session, List<ProblemInfo> problemInfos) {
        List<Tasks> tasks = new ArrayList<>();
        for (ProblemInfo problemInfo : problemInfos) {
            Tasks task = new Tasks();
            task.setSession(session);
            task.setName(problemInfo.getName());
            task.setTask(problemInfo.getProblem());
            task.setPoints(problemInfo.getPoints());

            tasks.add(task);
            tasksRepository.save(task);
        }
        return tasks;
    }

    @Transactional
    public LinkedList<Tasks> deleteProblem(DeleteProblemRequest deleteProblemRequest) {
        tasksRepository.deleteByIdAndSession(deleteProblemRequest.getId(), deleteProblemRequest.getSession());
        return tasksRepository.findAllBySession(deleteProblemRequest.getSession());
    }
}
