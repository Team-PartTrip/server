package com.example.PartTrip.profile.service;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.service.PlannerDeleteService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDeleteService {

    private final EntityManager em;
    private final GroupMemberRepository groupMemberRepository;
    private final TravelGroupRepository travelGroupRepository;
    private final PlannerDeleteService plannerDeleteService;
    private final ImageStorageService imageStorageService;

    @Transactional
    public void deleteAccount(String userId) {
        leavePlanners(userId);

        List<Long> cardIds = em.createQuery(
                        "select c.tripCardId from TripCardEntity c where c.userId = :u", Long.class)
                .setParameter("u", userId).getResultList();
        List<String> files = new ArrayList<>();
        if (!cardIds.isEmpty()) {
            files.addAll(em.createQuery(
                            "select p.imageUrl from TripCardPhotoEntity p where p.tripCardId in :ids", String.class)
                    .setParameter("ids", cardIds).getResultList());
            delete("delete from TripCardPhotoEntity p where p.tripCardId in :ids", "ids", cardIds);
            delete("delete from TripCardPlaceEntity p where p.tripCardId in :ids", "ids", cardIds);
            delete("delete from TripCardEntity c where c.tripCardId in :ids", "ids", cardIds);
        }
        files.addAll(em.createQuery(
                        "select u.imgUrl from UserEntity u where u.userId = :u", String.class)
                .setParameter("u", userId).getResultList());

        delete("delete from GroupInvitationEntity i where i.invitedUserId = :u or i.invitedByUserId = :u", "u", userId);
        delete("delete from GuardianLinkEntity l where l.seniorUserId = :u or l.guardianUserId = :u", "u", userId);
        delete("delete from GuardianInviteEntity i where i.seniorUserId = :u", "u", userId);
        delete("delete from UserLocationEntity l where l.userId = :u", "u", userId);
        delete("delete from NotificationEntity n where n.userId = :u", "u", userId);
        delete("delete from RefreshTokenEntity t where t.userId = :u", "u", userId);
        delete("delete from TravelPreferenceEntity p where p.userId = :u", "u", userId);
        delete("delete from UserEntity u where u.userId = :u", "u", userId);

        // 롤백되면 기록은 남는데 사진만 사라지지 않게, 커밋을 확인하고 지운다
        List<String> mine = files.stream().filter(Objects::nonNull).distinct().toList();
        Set<String> stillUsed = new HashSet<>();
        if (!mine.isEmpty()) {
            for (String jpql : List.of(
                    "select u.imgUrl from UserEntity u where u.imgUrl in :f",
                    "select p.imageUrl from TripCardPhotoEntity p where p.imageUrl in :f",
                    "select c.coverImageUrl from TripCardEntity c where c.coverImageUrl in :f")) {
                stillUsed.addAll(em.createQuery(jpql, String.class).setParameter("f", mine).getResultList());
            }
        }
        List<String> toRemove = mine.stream().filter(url -> !stillUsed.contains(url)).toList();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                toRemove.forEach(url -> {
                    if (!imageStorageService.delete(url)) {
                        log.warn("탈퇴한 사용자의 파일을 지우지 못했습니다: {}", url);
                    }
                });
            }
        });
    }

    private void leavePlanners(String userId) {
        for (GroupMemberEntity me : groupMemberRepository.findByUserId(userId)) {
            Long groupId = me.getGroupId();
            if (me.getRole() != GroupRole.OWNER) {
                groupMemberRepository.delete(me);
                continue;
            }
            GroupMemberEntity next = groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(groupId).stream()
                    .filter(m -> !m.getUserId().equals(userId))
                    .findFirst().orElse(null);
            if (next == null) {
                plannerDeleteService.deletePlanner(groupId, userId);
                continue;
            }
            next.setRole(GroupRole.OWNER);
            travelGroupRepository.findById(groupId).ifPresent(g -> g.setOwnerUserId(next.getUserId()));
            groupMemberRepository.delete(me);
        }
        groupMemberRepository.flush();
    }

    private void delete(String jpql, String name, Object value) {
        em.createQuery(jpql).setParameter(name, value).executeUpdate();
    }
}
