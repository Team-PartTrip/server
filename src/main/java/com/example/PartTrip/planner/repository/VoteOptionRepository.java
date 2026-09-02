package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.VoteOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteOptionRepository extends JpaRepository<VoteOptionEntity, Long> {

    List<VoteOptionEntity> findByVoteIdOrderByCreatedAtAsc(Long voteId);

    List<VoteOptionEntity> findByVoteIdInOrderByCreatedAtAsc(List<Long> voteIds);

    // 삭제 시 어느 투표의 후보인지까지 확인한다
    Optional<VoteOptionEntity> findByOptionIdAndVoteId(Long optionId, Long voteId);

    boolean existsByVoteIdAndTourPlaceId(Long voteId, Long tourPlaceId);

    boolean existsByVoteIdAndPlaceNameIgnoreCase(Long voteId, String placeName);

    void deleteByVoteId(Long voteId);

    // 플래너 삭제용
    void deleteByVoteIdIn(List<Long> voteIds);

    /**
     * 후보와 그 후보가 가리키는 관광지를 한 번에 가져온다.
     *
     * 예전에는 후보를 받은 뒤 tourPlaceId 를 모아 관광지를 또 물었다.
     * 관광지가 없는 직접 입력 후보도 있어서 LEFT JOIN 이다.
     *
     * 한 번에 담은 후보들은 createdAt 이 같아서 그것만으로는 순서가 그때그때
     * 달라진다. 화면에 후보가 뒤섞여 보이므로 optionId 로 순서를 고정한다.
     */
    @Query("""
            SELECT o, t FROM VoteOptionEntity o
             LEFT JOIN TourPlaceEntity t ON t.tourPlaceId = o.tourPlaceId
             WHERE o.voteId IN :voteIds
             ORDER BY o.createdAt ASC, o.optionId ASC
            """)
    List<Object[]> findOptionsWithPlaces(@Param("voteIds") List<Long> voteIds);
}
