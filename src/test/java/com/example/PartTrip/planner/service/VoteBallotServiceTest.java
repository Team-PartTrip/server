package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.notification.event.VoteParticipatedEvent;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 장바구니 없이 장소 목록에서 바로, 한 카테고리에서 여러 곳에 투표한다 (#127).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VoteBallotServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private PlannerCityRepository plannerCityRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteOptionRepository voteOptionRepository;
    @Mock private VoteRecordRepository voteRecordRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private VoteBallotService service;

    private TravelGroupEntity group;

    @BeforeEach
    void setUp() {
        group = new TravelGroupEntity();
        group.setGroupId(1L);
        group.setStatus(GroupStatus.PLANNING);
        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "me")).thenReturn(true);

        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setCityName("오사카");
        when(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(plan));
        when(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L)).thenReturn(List.of());

        when(tourPlaceRepository.findById(100L)).thenReturn(Optional.of(place(100L, "오사카")));
        when(tourPlaceRepository.findById(200L)).thenReturn(Optional.of(place(200L, "도쿄")));

        // 저장하면 id 를 붙여 돌려준다
        when(voteRepository.save(any())).thenAnswer(inv -> {
            VoteEntity v = inv.getArgument(0);
            v.setVoteId(50L);
            return v;
        });
        when(voteOptionRepository.save(any())).thenAnswer(inv -> {
            VoteOptionEntity o = inv.getArgument(0);
            o.setOptionId(70L);
            return o;
        });
        when(voteRecordRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    private static TourPlaceEntity place(Long id, String city) {
        TourPlaceEntity p = new TourPlaceEntity();
        p.setTourPlaceId(id);
        p.setCityName(city);
        p.setPlaceName("이치란 라멘");
        p.setCategory(TourPlaceCategory.RESTAURANT);
        return p;
    }

    private static VoteEntity openVote() {
        VoteEntity v = new VoteEntity();
        v.setVoteId(50L);
        v.setPlanId(10L);
        v.setCategory(TourPlaceCategory.RESTAURANT);
        v.setStatus(VoteStatus.OPEN);
        return v;
    }

    private static VoteOptionEntity option() {
        VoteOptionEntity o = new VoteOptionEntity();
        o.setOptionId(70L);
        o.setVoteId(50L);
        o.setTourPlaceId(100L);
        o.setPlaceName("이치란 라멘");
        return o;
    }

    @Test
    @DisplayName("장바구니 없이 처음 투표하면 카테고리 투표와 후보가 그때 생긴다")
    void createsVoteAndOptionOnFirstBallot() {
        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.empty());
        when(voteOptionRepository.findByVoteIdAndTourPlaceId(50L, 100L)).thenReturn(Optional.empty());
        when(voteRecordRepository.findByOptionIdAndUserId(70L, "me")).thenReturn(Optional.empty());

        service.voteForPlace(1L, 100L, "me");

        verify(voteRepository).save(any(VoteEntity.class));
        verify(voteOptionRepository).save(any(VoteOptionEntity.class));
        verify(voteRecordRepository).save(any(VoteRecordEntity.class));
        assertThat(group.getStatus()).isEqualTo(GroupStatus.VOTING);
    }

    @Test
    @DisplayName("같은 카테고리의 두 번째 장소에도 투표할 수 있고, 참여 알림은 처음 한 번만 간다")
    void secondPlaceInSameCategoryDoesNotRenotify() {
        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.of(openVote()));
        when(voteOptionRepository.findByVoteIdAndTourPlaceId(50L, 100L)).thenReturn(Optional.of(option()));
        when(voteRecordRepository.findByOptionIdAndUserId(70L, "me")).thenReturn(Optional.empty());
        // 이 카테고리에 이미 다른 장소로 투표했다
        when(voteRecordRepository.existsByVoteIdAndUserId(50L, "me")).thenReturn(true);

        service.voteForPlace(1L, 100L, "me");

        verify(voteRecordRepository).save(any(VoteRecordEntity.class));
        verify(eventPublisher, never()).publishEvent(any(VoteParticipatedEvent.class));
    }

    @Test
    @DisplayName("이 카테고리에 처음 투표하면 참여 알림이 간다")
    void firstBallotInCategoryNotifies() {
        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.of(openVote()));
        when(voteOptionRepository.findByVoteIdAndTourPlaceId(50L, 100L)).thenReturn(Optional.of(option()));
        when(voteRecordRepository.findByOptionIdAndUserId(70L, "me")).thenReturn(Optional.empty());
        when(voteRecordRepository.existsByVoteIdAndUserId(50L, "me")).thenReturn(false);

        service.voteForPlace(1L, 100L, "me");

        verify(eventPublisher).publishEvent(any(VoteParticipatedEvent.class));
    }

    @Test
    @DisplayName("같은 장소에 다시 누르면 표가 늘지 않는다")
    void sameePlaceTwiceIsIdempotent() {
        VoteRecordEntity existing = new VoteRecordEntity();
        existing.setVoteId(50L);
        existing.setOptionId(70L);
        existing.setUserId("me");
        existing.setVotedAt(LocalDateTime.now());

        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.of(openVote()));
        when(voteOptionRepository.findByVoteIdAndTourPlaceId(50L, 100L)).thenReturn(Optional.of(option()));
        when(voteRecordRepository.findByOptionIdAndUserId(70L, "me")).thenReturn(Optional.of(existing));

        service.voteForPlace(1L, 100L, "me");

        verify(voteRecordRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("이 여행의 도시가 아닌 장소에는 투표할 수 없다")
    void rejectsPlaceOutsideTripCities() {
        assertThatThrownBy(() -> service.voteForPlace(1L, 200L, "me"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이 여행의 도시");
    }

    @Test
    @DisplayName("멤버가 아니면 투표할 수 없다")
    void rejectsNonMember() {
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "outsider")).thenReturn(false);

        assertThatThrownBy(() -> service.voteForPlace(1L, 100L, "outsider"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("멤버");
    }

    @Test
    @DisplayName("표를 거두면 그 장소에 던진 내 표만 지운다")
    void cancelDeletesOnlyMyBallotOnThatPlace() {
        VoteRecordEntity mine = new VoteRecordEntity();
        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.of(openVote()));
        when(voteOptionRepository.findByVoteIdAndTourPlaceId(50L, 100L)).thenReturn(Optional.of(option()));
        when(voteRecordRepository.findByOptionIdAndUserId(70L, "me")).thenReturn(Optional.of(mine));

        service.cancelPlaceVote(1L, 100L, "me");

        verify(voteRecordRepository).delete(mine);
    }

    @Test
    @DisplayName("투표한 적 없는 장소의 표를 거둬도 아무 일도 없다")
    void cancelWithoutBallotDoesNothing() {
        when(voteRepository.findByPlanIdAndCategory(10L, TourPlaceCategory.RESTAURANT))
                .thenReturn(Optional.empty());

        service.cancelPlaceVote(1L, 100L, "me");

        verify(voteRecordRepository, never()).delete(any());
    }
}
