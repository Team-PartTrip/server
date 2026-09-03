package com.example.PartTrip.notification.scheduler;

import com.example.PartTrip.notification.event.VoteDeadlineEvent;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

// 투표 마감 임박 알림 (NotificationType.VOTE_DEADLINE)
//
// 다른 알림과 달리 사용자 행동이 아니라 시간으로 발생하므로 발행하는 쪽이 없다.
@Slf4j
@Component
@RequiredArgsConstructor
public class VoteDeadlineScheduler {

    private final VoteRepository voteRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 마감 몇 시간 전에 알릴지
    private static final int NOTIFY_BEFORE_HOURS = 24;

    // 매시 정각. 아래 조회 구간이 한 시간이므로 주기도 한 시간이어야 한다.
    // 값을 밖으로 뺀 것은 운영 중에 주기를 바꾸거나 테스트에서 짧게 돌리기 위해서다.
    @Scheduled(cron = "${part-trip.notification.vote-deadline-cron:0 0 * * * *}")
    public void publishDeadlineSoon() {

        LocalDateTime now = LocalDateTime.now();

        // 마감 24시간 전부터 23시간 전 사이의 투표만 고른다.
        // 구간은 from 을 넣고 to 를 뺀다. 양끝을 다 넣으면 앞 실행의 끝과
        // 다음 실행의 시작이 같은 시각이라 그 경계의 투표가 두 번 잡힌다.
        // 각 투표는 이 한 시간 구간을 정확히 한 번만 지나간다.
        // 별도 발송 여부 컬럼을 두지 않으려고 이렇게 잡았다.
        // 서버가 그 시간대에 꺼져 있으면 해당 투표는 건너뛴다.
        LocalDateTime from = now.plusHours(NOTIFY_BEFORE_HOURS - 1);
        LocalDateTime to = now.plusHours(NOTIFY_BEFORE_HOURS);

        try {
            List<VoteEntity> votes =
                    voteRepository
                            .findByStatusAndDeadlineGreaterThanEqualAndDeadlineLessThan(
                                    VoteStatus.OPEN, from, to);

            if (votes.isEmpty()) {
                return;
            }

            for (VoteEntity vote : votes) {
                eventPublisher.publishEvent(new VoteDeadlineEvent(vote.getVoteId()));
            }

            log.info("투표 마감 임박 알림 발행 {}건", votes.size());

        } catch (Exception e) {
            // 여기서 예외가 나가면 스케줄러가 매시 스택트레이스를 남긴다.
            // 다음 시간에 다시 시도하면 되므로 경고만 남긴다.
            log.warn("투표 마감 임박 알림 발행 실패", e);
        }
    }
}
