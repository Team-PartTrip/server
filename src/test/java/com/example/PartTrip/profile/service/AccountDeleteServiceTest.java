package com.example.PartTrip.profile.service;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.guardian.entity.GuardianLinkEntity;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.service.PlannerDeleteService;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.transaction.TestTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({AccountDeleteService.class, PlannerDeleteService.class})
class AccountDeleteServiceTest {

    @Autowired AccountDeleteService service;
    @Autowired EntityManager em;
    @MockitoBean ImageStorageService storage;

    private static final LocalDateTime T = LocalDateTime.of(2026, 9, 1, 9, 0);

    private void user(String id, String imgUrl) {
        UserEntity u = new UserEntity();
        u.setImgUrl(imgUrl);
        u.setUserId(id);
        u.setNickName("nick-" + id);
        em.persist(u);
    }

    private Long group(String owner) {
        TravelGroupEntity g = new TravelGroupEntity();
        g.setOwnerUserId(owner);
        g.setHeadcount(2);
        g.setStatus(GroupStatus.PLANNING);
        g.setCreatedAt(T);
        em.persist(g);
        return g.getGroupId();
    }

    private void member(Long groupId, String userId, GroupRole role, int minute) {
        GroupMemberEntity m = new GroupMemberEntity();
        m.setGroupId(groupId);
        m.setUserId(userId);
        m.setRole(role);
        m.setJoinedAt(T.plusMinutes(minute));
        em.persist(m);
    }

    private Long card(String userId) {
        TripCardEntity c = TripCardEntity.builder().userId(userId).title("경주")
                .regionCode("47").startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 2)).createdAt(T).build();
        em.persist(c);
        TripCardPhotoEntity p = new TripCardPhotoEntity();
        p.setTripCardId(c.getTripCardId());
        p.setImageUrl("/uploads/trip-card/" + userId + ".jpg");
        em.persist(p);
        return c.getTripCardId();
    }

    private long count(String jpql, String userId) {
        return em.createQuery(jpql, Long.class).setParameter("u", userId).getSingleResult();
    }

    @Test
    void 탈퇴하면_내_데이터는_지우고_함께_쓰던_플래너는_남은_사람에게_넘긴다() {
        user("me", "/uploads/trip-card/friend.jpg");
        user("friend", "/uploads/profile/friend.jpg");
        Long shared = group("me");
        member(shared, "me", GroupRole.OWNER, 0);
        member(shared, "friend", GroupRole.MEMBER, 5);
        Long solo = group("me");
        member(solo, "me", GroupRole.OWNER, 0);
        Long friendsTrip = group("friend");
        member(friendsTrip, "friend", GroupRole.OWNER, 0);
        member(friendsTrip, "me", GroupRole.MEMBER, 5);
        card("me");
        Long friendCard = card("friend");
        em.persist(new GuardianLinkEntity("me", "friend", T));
        em.flush();
        em.clear();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        when(storage.delete(anyString())).thenReturn(true);

        service.deleteAccount("me");

        verify(storage).delete("/uploads/trip-card/me.jpg");
        verify(storage, never()).delete("/uploads/trip-card/friend.jpg");
        verify(storage, never()).delete("/uploads/profile/friend.jpg");
        TestTransaction.start();

        assertThat(em.find(UserEntity.class, "me")).isNull();
        assertThat(count("select count(c) from TripCardEntity c where c.userId = :u", "me")).isZero();
        assertThat(count("select count(m) from GroupMemberEntity m where m.userId = :u", "me")).isZero();
        assertThat(count("select count(l) from GuardianLinkEntity l where l.guardianUserId = :u", "friend")).isZero();
        assertThat(em.find(TravelGroupEntity.class, solo)).isNull();

        TravelGroupEntity kept = em.find(TravelGroupEntity.class, shared);
        assertThat(kept.getOwnerUserId()).isEqualTo("friend");
        assertThat(em.createQuery("select m.role from GroupMemberEntity m where m.groupId = :g and m.userId = 'friend'", GroupRole.class)
                .setParameter("g", shared).getSingleResult()).isEqualTo(GroupRole.OWNER);

        assertThat(em.find(TripCardEntity.class, friendCard)).isNotNull();
        assertThat(count("select count(p) from TripCardPhotoEntity p where p.imageUrl like concat('%', :u, '%')", "me")).isZero();
        assertThat(count("select count(p) from TripCardPhotoEntity p where p.imageUrl like concat('%', :u, '%')", "friend")).isOne();

        // 커밋한 데이터가 다른 테스트에 남지 않게 지운다
        service.deleteAccount("friend");
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}
