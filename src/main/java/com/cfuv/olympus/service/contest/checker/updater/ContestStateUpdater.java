package com.cfuv.olympus.service.contest.checker.updater;

import com.cfuv.olympus.domain.contest.Contest;
import com.cfuv.olympus.domain.contest.ContestState;
import com.cfuv.olympus.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
public class ContestStateUpdater implements ApplicationListener<ContextRefreshedEvent> {
    private final ContestRepository contestRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Contest> contests = contestRepository.findAll();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+3"));
        for (Contest contest : contests) {
            if (contest.getState() == ContestState.IN_PROGRESS && contest.getEndTime().isBefore(now)) {
                contest.setState(ContestState.FINISHED);
                contestRepository.save(contest);
            }
            else if (contest.getState() == ContestState.IN_PROGRESS && contest.getEndTime().isAfter(now)) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        contest.setState(ContestState.FINISHED);
                        contestRepository.save(contest);
                        timer.cancel();
                    }
                }, Date.from(contest.getEndTime().toInstant()));
            }
        }
    }
}
