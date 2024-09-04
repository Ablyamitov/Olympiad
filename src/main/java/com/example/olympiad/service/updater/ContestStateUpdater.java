package com.example.olympiad.service.updater;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.ContestState;
import com.example.olympiad.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContestStateUpdater implements ApplicationListener<ContextRefreshedEvent> {
    private final ContestRepository contestRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Contest> contests = contestRepository.findAll();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+3"));
        for (Contest contest : contests) {
            if (contest.getEndTime()!=null && contest.getEndTime().isBefore(now) && contest.getState() != ContestState.FINISHED) {
                contest.setState(ContestState.FINISHED);
                contestRepository.save(contest);
            }
        }
    }
}
