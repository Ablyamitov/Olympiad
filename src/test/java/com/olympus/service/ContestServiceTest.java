package com.olympus.service;

import com.cfuv.olympus.repository.ContestRepository;
import com.cfuv.olympus.repository.TasksRepository;
import com.cfuv.olympus.service.ContestService;
import com.cfuv.olympus.service.TaskService;
import com.cfuv.olympus.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.mockito.ArgumentMatchers.any;

public class ContestServiceTest {
    @Mock
    private ContestRepository contestRepository;

    @Mock
    private TasksRepository tasksRepository;


    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;


    @InjectMocks
    private ContestService contestService;


//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetContestOptionalBySession() {
//        Contest contest = new Contest();
//        contest.setStartTime(ZonedDateTime.now());
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//
//        Contest result = contestService.getContestOptionalBySession(1L);
//
//        assertEquals(contest, result);
//    }
//
//    @Test
//    void testGetContestOptionalBySessionNotStarted() {
//        Contest contest = new Contest();
//        contest.setStartTime(null);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//
//        assertThrows(ContestNotStartedException.class, () -> {
//            contestService.getContestOptionalBySession(1L);
//        });
//    }
//
//    @Test
//    void testGetContestOptionalBySessionNotFound() {
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            contestService.getContestOptionalBySession(1L);
//        });
//    }
//
//    @Test
//    void testCreateContest() throws IOException {
//        ContestRequest contestRequest = new ContestRequest();
//        contestRequest.setName("Test Contest");
//        contestRequest.setParticipantCount(10);
//        contestRequest.setJudgeCount(5);
//        contestRequest.setUsernamePrefix("user");
//        contestRequest.setDuration("02:00");
//
//        Contest contest = new Contest();
//        contest.setSession(1L);
//        contest.setName("Test Contest");
//        contest.setParticipantCount(10);
//        contest.setJudgeCount(5);
//        contest.setUsernamePrefix("user");
//        contest.setDuration("02:00");
//        contest.setState(ContestState.NOT_STARTED);
//
//        when(contestRepository.findContestWithMaxSession()).thenReturn(Optional.empty());
//        when(contestRepository.save(any(Contest.class))).thenReturn(contest);
//
//        Map<User, String> participants = new HashMap<>();
//        Map<User, String> judges = new HashMap<>();
//        when(userService.createParticipants(10, "user", 1L)).thenReturn(participants);
//        when(userService.createJudges(5, "user", 1L)).thenReturn(judges);
//
//
//        EmailService emailService = mock(EmailService.class);
//        ContestService contestService = new ContestService(contestRepository, tasksRepository, taskService, userService, emailService);
//
//        ContestAndFileResponse response = contestService.create(contestRequest);
//
//        assertNotNull(response);
//        assertEquals(contest, response.getContest());
//    }
//
//    @Test
//    void testCreateUsers() throws IOException {
//        CreateUsersRequest createUsersRequest = new CreateUsersRequest();
//        createUsersRequest.setSession(1L);
//        createUsersRequest.setParticipantCount(10);
//        createUsersRequest.setJudgeCount(5);
//
//        Contest contest = new Contest();
//        contest.setSession(1L);
//        contest.setName("Test Contest");
//        contest.setParticipantCount(10);
//        contest.setJudgeCount(5);
//        contest.setUsernamePrefix("user");
//        contest.setDuration("02:00");
//        contest.setState(ContestState.NOT_STARTED);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//        when(contestRepository.save(any(Contest.class))).thenReturn(contest);
//
//        Map<User, String> participants = new HashMap<>();
//        Map<User, String> judges = new HashMap<>();
//        when(userService.createParticipants(10, "user", 1L, 10)).thenReturn(participants);
//        when(userService.createJudges(5, "user", 1L, 5)).thenReturn(judges);
//
//        EmailService emailService = mock(EmailService.class);
//        ContestService contestService = new ContestService(contestRepository, tasksRepository, taskService, userService, emailService);
//        FileResponse response = contestService.createUsers(createUsersRequest);
//
//        assertNotNull(response);
//    }

//    @Test
//    void testStart() {
//        Contest contest = new Contest();
//        contest.setSession(1L);
//        contest.setDuration("02:00");
//        contest.setState(ContestState.NOT_STARTED);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//
//        GetStartAndEndContestTimeResponse response = contestService.start(1L);
//
//        assertNotNull(response);
//        assertNotNull(response.getStartTime());
//        assertNotNull(response.getEndTime());
//    }
//
//    @Test
//    void testDeleteContest() {
//        Contest contest = new Contest();
//        contest.setSession(1L);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//
//        contestService.deleteContest(1L);
//
//        verify(contestRepository, times(1)).delete(contest);
//    }
//
//    @Test
//    void testGetAllContests() {
//        int page = 1;
//        Page<Contest> pagedResult = mock(Page.class);
//        when(contestRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
//        when(pagedResult.getContent()).thenReturn(Collections.emptyList());
//        when(contestRepository.count()).thenReturn(0L);
//
//        GetAllContestsResponse response = contestService.getAllContests(page);
//
//        assertNotNull(response);
//        assertEquals(0, response.getContestsInfos().size());
//        assertEquals(0, response.getCount());
//    }
//    @Test
//    void testGetAllContestsContainingNameOrState() {
//        int page = 1;
//        String name = "Test";
//        String statesString = "NOT_STARTED,IN_PROGRESS";
//        List<ContestState> states = Arrays.asList(ContestState.NOT_STARTED, ContestState.IN_PROGRESS);
//
//        Page<Contest> pagedResult = mock(Page.class);
//        when(contestRepository.findByNameContainingAndStateIn(eq(name), eq(states), any(Pageable.class))).thenReturn(pagedResult);
//        when(pagedResult.getContent()).thenReturn(Collections.emptyList());
//        when(contestRepository.count()).thenReturn(0L);
//
//        GetAllContestsResponse response = contestService.getAllContestsContainingNameOrState(page, name, statesString);
//
//        assertNotNull(response);
//        assertEquals(0, response.getContestsInfos().size());
//        assertEquals(0, response.getCount());
//    }
//
//    @Test
//    void testGetContestBySession() {
//        Contest contest = new Contest();
//        contest.setSession(1L);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//
//        Contest result = contestService.getContestBySession(1L);
//
//        assertEquals(contest, result);
//    }
//
//    @Test
//    void testChangeDuration() {
//        ChangeDurationRequest changeDurationRequest = new ChangeDurationRequest();
//        changeDurationRequest.setSession(1L);
//        changeDurationRequest.setNewDuration("03:00");
//
//        Contest contest = new Contest();
//        contest.setSession(1L);
//        contest.setState(ContestState.NOT_STARTED);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//        when(contestRepository.save(any(Contest.class))).thenReturn(contest);
//
//        String newDuration = contestService.changeDuration(changeDurationRequest);
//
//        assertEquals("03:00", newDuration);
//    }
//
//    @Test
//    void testChangeName() {
//        ChangeNameRequest changeNameRequest = new ChangeNameRequest();
//        changeNameRequest.setSession(1L);
//        changeNameRequest.setName("New Contest Name");
//
//        Contest contest = new Contest();
//        contest.setSession(1L);
//        contest.setState(ContestState.NOT_STARTED);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//        when(contestRepository.save(any(Contest.class))).thenReturn(contest);
//
//        String newName = contestService.changeName(changeNameRequest);
//
//        assertEquals("New Contest Name", newName);
//    }
//
//    @Test
//    void testAddProblems() throws IOException {
//        AddProblemRequest addProblemRequest = new AddProblemRequest();
//        addProblemRequest.setSession(1L);
//        addProblemRequest.setHtmlContent("<html>Problem</html>");
//        addProblemRequest.setName(null);
//        addProblemRequest.setHtmlName("problem1.html");
//        addProblemRequest.setPoints(10);
//
//        Contest contest = new Contest();
//        contest.setSession(1L);
//
//        when(contestRepository.findBySession(1L)).thenReturn(Optional.of(contest));
//        when(tasksRepository.save(any(Tasks.class))).thenReturn(new Tasks());
//
//        List<Tasks> tasks = contestService.addProblems(addProblemRequest);
//
//        assertNotNull(tasks);
//    }
//
//    @Test
//    void testDeleteProblem() {
//        DeleteProblemRequest deleteProblemRequest = new DeleteProblemRequest();
//        deleteProblemRequest.setSession(1L);
//        deleteProblemRequest.setId(1L);
//        LinkedList<Tasks> tasks = new LinkedList<>();
//        tasks.add(new Tasks());
//
//        when(tasksRepository.findAllBySession(1L)).thenReturn(tasks);
//
//        LinkedList<Tasks> result = contestService.deleteProblem(deleteProblemRequest);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }

}
