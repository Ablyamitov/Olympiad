package com.cfuv.olympus.service;

import com.cfuv.olympus.domain.contest.ContestState;
import com.cfuv.olympus.domain.contest.Tasks;
import com.cfuv.olympus.domain.exception.entity.contest.ContestNotFoundException;
import com.cfuv.olympus.repository.ContestRepository;
import com.cfuv.olympus.web.dto.contest.GetAllContests.GetAllContestsResponse;
import com.cfuv.olympus.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.cfuv.olympus.domain.contest.Contest;
import com.cfuv.olympus.domain.exception.entity.contest.ContestNotStartedException;
import com.cfuv.olympus.domain.exception.entity.contest.ContestStartedYetException;
import com.cfuv.olympus.domain.exception.entity.task.NoTasksException;
import com.cfuv.olympus.domain.mail.EmailDetails;
import com.cfuv.olympus.domain.user.User;
import com.cfuv.olympus.repository.TasksRepository;
import com.cfuv.olympus.service.mail.EmailService;
import com.cfuv.olympus.web.dto.contest.ChangeDuration.ChangeDurationRequest;
import com.cfuv.olympus.web.dto.contest.ChangeName.ChangeNameRequest;
import com.cfuv.olympus.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.cfuv.olympus.web.dto.contest.CreateContest.ContestRequest;
import com.cfuv.olympus.web.dto.contest.EditProblems.AddProblemRequest;
import com.cfuv.olympus.web.dto.contest.EditProblems.DeleteProblemRequest;
import com.cfuv.olympus.web.dto.contest.GetAllContests.ContestsInfo;
import com.cfuv.olympus.web.dto.contest.createUsers.CreateUsersRequest;
import com.cfuv.olympus.web.dto.contest.createUsers.FileResponse;
import com.github.junrar.exception.RarException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final TasksRepository tasksRepository;
    private final TaskService taskService;
    private final UserTaskService userTaskService;

    private final UserService userService;
    private final EmailService emailService;

    @Value("${storage.location}")
    private String UPLOAD_DIR;

    @Value("${spring.mail.username}")
    private String toAddress;
    public static final Logger log = LoggerFactory.getLogger(ContestService.class);


    @Retryable(retryFor = ContestNotStartedException.class,
            maxAttempts = 11,
            backoff = @Backoff(delay = 5000))
    public Contest getContestOptionalBySession(Long session) {
        Contest contest = contestRepository.findBySession(session)
                .orElseThrow(() -> new EntityNotFoundException("Олимпиады не существует"));

        if (contest.getStartTime() != null) return contest;
        else throw new ContestNotStartedException("Олимпиада ещё не начата");
    }


    @Transactional
    public ContestAndFileResponse create(final ContestRequest contestRequest) throws IOException {
        Contest contest = createContest(contestRequest);
        Map<User, String> participants = userService.createParticipants(contest.getParticipantCount(), contest.getUsernamePrefix(), contest.getSession());
        Map<User, String> judges = userService.createJudges(contest.getJudgeCount(), contest.getUsernamePrefix(), contest.getSession());
        contestRepository.save(contest);
        return createContestAndFileResponse(contest, participants, judges);
    }


    @Transactional
    public FileResponse createUsers(final CreateUsersRequest createUsersRequest) throws IOException {
        Contest contest = contestRepository.findBySession(createUsersRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));
        Map<User, String> participants = userService.createParticipants(createUsersRequest.getParticipantCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(), contest.getParticipantCount());
        Map<User, String> judges = userService.createJudges(createUsersRequest.getJudgeCount(), contest.getUsernamePrefix(), createUsersRequest.getSession(), contest.getJudgeCount());
        contest.setParticipantCount(contest.getParticipantCount() + createUsersRequest.getParticipantCount());
        contest.setJudgeCount(contest.getJudgeCount() + createUsersRequest.getJudgeCount());
        contestRepository.save(contest);
        return createFileResponse(contest, participants, judges);
    }

    private Contest createContest(ContestRequest contestRequest) {
        Contest contest = new Contest();
        long maxSession = contestRepository.findContestWithMaxSession().map(c -> c.getSession() + 1).orElse(1L);
        contest.setSession(maxSession);
        contest.setName(contestRequest.getName());
        contest.setParticipantCount(contestRequest.getParticipantCount());
        contest.setJudgeCount(contestRequest.getJudgeCount());
        contest.setUsernamePrefix(contestRequest.getUsernamePrefix());
        contest.setDuration(contestRequest.getDuration());
        contest.setState(ContestState.NOT_STARTED);
        return contest;
    }

    private ContestAndFileResponse createContestAndFileResponse(Contest contest, Map<User, String> participants, Map<User, String> judges) throws IOException {
        File file = createFile(contest, participants, judges);
        assert file != null;
        byte[] fileContent = Files.readAllBytes(file.toPath());
        ContestAndFileResponse response = new ContestAndFileResponse();
        response.setContest(contest);
        response.setFileContent(fileContent);
        if (!file.delete()) log.info("Файл " + file.getName() + " не может быть удален");
        return response;
    }

    private FileResponse createFileResponse(Contest contest, Map<User, String> participants, Map<User, String> judges) throws IOException {
        File file = createFile(contest, participants, judges);
        assert file != null;
        byte[] fileContent = Files.readAllBytes(file.toPath());
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
            log.error("Ошибка с файлом");
        }
        return null;
    }

    public GetStartAndEndContestTimeResponse start(final Long contestSession) {
        Contest contest = contestRepository.findBySession(contestSession)
                .orElseThrow(() ->
                        new ContestNotFoundException("Олимпиада не найдена"));
        if (contest.getStartTime() != null) {
            throw new ContestStartedYetException("Олимпиада уже начата");
        }

        if (contest.getTasks() == null || contest.getTasks().isEmpty()) {
            throw new NoTasksException("Невозможно начать олимпиаду без заданий");
        }

        ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC+3"));
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
                timer.cancel();
            }
        }, Date.from(endTime.toInstant()));

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
    public void deleteContest(Long session) {
        Contest contest = contestRepository.findBySession(session)
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));
        userService.deleteParticipantsAndJudges(contest);
        userTaskService.deleteAllBySession(session);
        taskService.deleteAllBySession(session);

        // Определяем пути для удаления файлов
        String tasksDir = UPLOAD_DIR + "tasks/" + session;
        String userTasksDir = UPLOAD_DIR + "user-tasks/" + session;
        String imagesDir = "images/" + session;

        // Удаляем связанные файлы, если директории существуют
        try {
            deleteDirectory(Paths.get(tasksDir));
            deleteDirectory(Paths.get(imagesDir));
            deleteDirectory(Paths.get(userTasksDir));
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при удалении файлов: " + e.getMessage(), e);
        }


        contestRepository.delete(contest);
    }

    public GetAllContestsResponse getAllContests(Integer page) {
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC,"session"));

        Page<Contest> pagedResult = contestRepository.findAll(pageable);

        return getGetAllContests(pagedResult);
    }

    public GetAllContestsResponse getAllContestsContainingNameOrState(Integer page, String name, String statesString) {
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC,"session"));
        List<ContestState> states;
        if (name == null) name = "";
        if (statesString != null) {
            states = Arrays.stream(statesString.split(","))
                    .map(String::trim)
                    .map(ContestState::valueOf)
                    .collect(Collectors.toList());
        } else {
            states = new ArrayList<>(List.of(ContestState.NOT_STARTED, ContestState.IN_PROGRESS, ContestState.FINISHED));
        }

        Page<Contest> pagedResult = contestRepository.findByNameContainingAndStateIn(name, states, pageable);

        return getGetAllContests(pagedResult);
    }

    private GetAllContestsResponse getGetAllContests(Page<Contest> pagedResult) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
        List<ContestsInfo> contests = pagedResult.stream()
                .map(contest -> new ContestsInfo(
                        contest.getName(),
                        contest.getSession(),
                        contest.getState(),
                        contest.getDuration(),
                        (contest.getStartTime() != null) ? contest.getStartTime().plusHours(3).format(formatter) : null,
                        (contest.getEndTime() != null) ? contest.getEndTime().plusHours(3).format(formatter) : null))
                .collect(Collectors.toList());

        Long count = contestRepository.count();

        GetAllContestsResponse getAllContestsResponse = new GetAllContestsResponse();
        getAllContestsResponse.setContestsInfos(contests);
        getAllContestsResponse.setCount(count);

        return getAllContestsResponse;
    }


    public Contest getContestBySession(Long session) {
        return contestRepository.findBySession(session)
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));
    }


    @Transactional
    public String changeDuration(ChangeDurationRequest changeDurationRequest) {
        Contest contest = contestRepository.findBySession(changeDurationRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));
        if (contest.getState() == ContestState.IN_PROGRESS) {
            throw new ContestStartedYetException("Олимпиада уже начата");
        }
        contest.setDuration(changeDurationRequest.getNewDuration());
        contestRepository.save(contest);
        return contest.getDuration();
    }

    @Transactional
    public Boolean changeName(Long session, String name) {
        Contest contest = contestRepository.findBySession(session)
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));
        if (contest.getState() == ContestState.IN_PROGRESS) {
            throw new ContestStartedYetException("Олимпиада уже начата");
        }
        contest.setName(name);
        contestRepository.save(contest);
        return true;
    }

    @Transactional
    public List<Tasks> addProblems(AddProblemRequest addProblemRequest) throws IOException {
        Contest contest = contestRepository.findBySession(addProblemRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Олимпиады не существует"));


        Tasks task = new Tasks();
        Long idInSession = tasksRepository.countBySession(addProblemRequest.getSession()) + 1;
        task.setTaskId(idInSession);
        task.setSession(contest.getSession());
        task.setTask(addProblemRequest.getHtmlContent());
        task.setName(addProblemRequest.getName());
        task.setHtmlName(addProblemRequest.getHtmlName());
        task.setPoints(addProblemRequest.getPoints());
        tasksRepository.save(task);

//        Long lastId = tasksRepository.findMaxIdBySession(addProblemRequest.getSession());
//        Long lastId = tasksRepository.findFirstBySessionOrderByIdDesc(addProblemRequest.getSession())
//                .map(Tasks::getId)
//                .orElse(null);

        if (addProblemRequest.getProblem() != null && !addProblemRequest.getProblem().isEmpty()){
            try {
            String tasksDir = UPLOAD_DIR + "tasks" + "/" + addProblemRequest.getSession().toString() + "/" + idInSession + "/";
            Path tasksPath = Paths.get(tasksDir);
            if (!Files.exists(tasksPath)) {
                Files.createDirectories(tasksPath);
            }
            taskService.handleFile(addProblemRequest.getProblem().getInputStream(),
                    tasksDir,
                    addProblemRequest.getProblem().getOriginalFilename());
            } catch (IOException | RarException e) {
                throw new IOException(e.getMessage());
            }
        }
        if (addProblemRequest.getImages() != null && !addProblemRequest.getImages().isEmpty()) {
            try {
                String photoDir = "images/" + addProblemRequest.getSession() + "/" + idInSession + "/";
                Path photoPath = Paths.get(photoDir);
                if (!Files.exists(photoPath)) {
                    Files.createDirectories(photoPath);
                }
                taskService.handleAddProblemFile(addProblemRequest.getImages().getInputStream(),
                        photoDir,
                        addProblemRequest.getImages().getOriginalFilename());
            } catch (IOException | RarException e) {
                throw new IOException(e.getMessage());
            }

        }

//        if (addProblemRequest.getName() != null && addProblemRequest.getImages() != null) {
//            try {
//                //String userDir = UPLOAD_DIR + "tasks" + "/" + task.getSession().toString() + "/" + lastId.toString() + "/";
//                String photoDir = "images/" + addProblemRequest.getSession() + "/" + idInSession + "/";
//                String tasksDir = UPLOAD_DIR + "tasks" + "/" + task.getSession().toString() + "/" + idInSession + "/";
//                Path photoPath = Paths.get(photoDir);
//                Path tasksPath = Paths.get(tasksDir);
//                if (!Files.exists(photoPath)) {
//                    Files.createDirectories(photoPath);
//                }
//                if (!Files.exists(tasksPath)) {
//                    Files.createDirectories(tasksPath);
//                }
//
//                taskService.handleFile(addProblemRequest.getProblem().getInputStream(),
//                        tasksDir,
//                        addProblemRequest.getProblem().getOriginalFilename());
//                taskService.handleAddProblemFile(addProblemRequest.getImages().getInputStream(),
//                        photoDir,
//                        addProblemRequest.getImages().getOriginalFilename());
//            } catch (IOException | RarException e) {
//                throw new IOException(e.getMessage());
//            }
//        }

        List<Tasks> problems = tasksRepository.findAllBySession(contest.getSession());
        contest.setTasks(problems);
        contestRepository.save(contest);

        return problems;
    }


    @Transactional
    public LinkedList<Tasks> deleteProblem(DeleteProblemRequest deleteProblemRequest) {
//        tasksRepository.deleteByIdAndSession(deleteProblemRequest.getId(), deleteProblemRequest.getSession());
//        return tasksRepository.findAllBySession(deleteProblemRequest.getSession());

        // Находим задание по ID и получаем его IDinSession
        Tasks task = tasksRepository.findByIdAndSession(deleteProblemRequest.getId(), deleteProblemRequest.getSession())
                .orElseThrow(() -> new IllegalStateException("Задание не найдено"));

        Long idInSession = task.getTaskId(); // Предполагаем, что IDinSession хранится в поле taskId

        // Удаляем задание из базы данных
        tasksRepository.delete(task);

        // Определяем пути для удаления файлов
        String tasksDir = UPLOAD_DIR + "tasks/" + deleteProblemRequest.getSession() + "/" + idInSession + "/";
        String imagesDir = "images/" + deleteProblemRequest.getSession() + "/" + idInSession + "/";

        // Удаляем связанные файлы, если директории существуют
        try {
            deleteDirectory(Paths.get(tasksDir));
            deleteDirectory(Paths.get(imagesDir));
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при удалении файлов задания: " + e.getMessage(), e);
        }

        // Возвращаем обновленный список задач для этой сессии
        return tasksRepository.findAllBySession(deleteProblemRequest.getSession());
    }
    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path).sorted(Comparator.reverseOrder())) {
                walk.forEach(filePath -> {
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        throw new UncheckedIOException("Не удалось удалить файл или папку: " + filePath, e);
                    }
                });
            }
        }
    }

    public boolean isContestFinished(Long session) {
        Contest contest = contestRepository.findBySession(session)
                .orElseThrow(() -> new ContestNotFoundException("Олимпиада не найдена"));
        return contest.getState() == ContestState.FINISHED;
    }
}
