package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.VoteRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 투표 기록의 유니크 제약 (#127).
 *
 * 한 카테고리(vote)에서 여러 곳에 투표할 수 있어야 하고, 같은 장소(option)에
 * 두 번 투표할 수는 없어야 한다. 서비스가 막더라도 동시에 두 번 누르면
 * 서비스 검사를 둘 다 통과할 수 있어서, 마지막 방어는 DB 제약이다.
 */
@DataJpaTest
class VoteRecordRepositoryTest {

    @Autowired private VoteRecordRepository voteRecordRepository;

    private static VoteRecordEntity record(Long voteId, Long optionId, String userId) {
        VoteRecordEntity r = new VoteRecordEntity();
        r.setVoteId(voteId);
        r.setOptionId(optionId);
        r.setUserId(userId);
        r.setVotedAt(LocalDateTime.now());
        return r;
    }

    @Test
    @DisplayName("한 카테고리에서 여러 곳에 투표할 수 있다")
    void allowsSeveralOptionsInOneVote() {
        voteRecordRepository.saveAndFlush(record(1L, 10L, "me"));
        voteRecordRepository.saveAndFlush(record(1L, 11L, "me"));
        voteRecordRepository.saveAndFlush(record(1L, 12L, "me"));

        assertThat(voteRecordRepository.findByVoteId(1L)).hasSize(3);
        assertThat(voteRecordRepository.existsByVoteIdAndUserId(1L, "me")).isTrue();
    }

    @Test
    @DisplayName("같은 장소에 두 번 투표할 수는 없다")
    void rejectsSameOptionTwice() {
        voteRecordRepository.saveAndFlush(record(1L, 10L, "me"));

        assertThatThrownBy(() -> voteRecordRepository.saveAndFlush(record(1L, 10L, "me")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("같은 장소라도 다른 사람은 투표할 수 있다")
    void allowsOtherUserOnSameOption() {
        voteRecordRepository.saveAndFlush(record(1L, 10L, "me"));
        voteRecordRepository.saveAndFlush(record(1L, 10L, "friend"));

        assertThat(voteRecordRepository.findByVoteId(1L)).hasSize(2);
    }
}
