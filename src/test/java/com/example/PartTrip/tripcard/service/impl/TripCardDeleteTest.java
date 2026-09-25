package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.global.security.CurrentUserProvider;
import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(TripCardServiceImpl.class)
class TripCardDeleteTest {

    @Autowired TripCardServiceImpl service;
    @Autowired EntityManager em;
    @MockitoBean CurrentUserProvider currentUserProvider;
    @MockitoBean ImageStorageService imageStorageService;

    private Long card(String userId) {
        TripCardEntity c = TripCardEntity.builder().userId(userId).title("경주")
                .countryName("대한민국").startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 2)).createdAt(LocalDateTime.of(2026, 9, 1, 9, 0)).build();
        em.persist(c);
        TripCardPlaceEntity place = new TripCardPlaceEntity();
        place.setTripCardId(c.getTripCardId());
        place.setPlaceName("불국사");
        place.setVisitedDate(LocalDate.of(2026, 9, 1));
        em.persist(place);
        TripCardPhotoEntity photo = new TripCardPhotoEntity();
        photo.setTripCardId(c.getTripCardId());
        photo.setImageUrl("/uploads/trip-card/" + userId + ".jpg");
        em.persist(photo);
        return c.getTripCardId();
    }

    private long children(Long cardId) {
        return em.createQuery("select count(p) from TripCardPhotoEntity p where p.tripCardId = :c", Long.class)
                .setParameter("c", cardId).getSingleResult()
                + em.createQuery("select count(p) from TripCardPlaceEntity p where p.tripCardId = :c", Long.class)
                .setParameter("c", cardId).getSingleResult();
    }

    @Test
    void 카드를_지우면_사진과_장소_행도_지워지고_다른_카드는_남는다() {
        Long mine = card("me");
        Long other = card("friend");
        em.flush();
        em.clear();
        when(currentUserProvider.getCurrentUserId()).thenReturn("me");

        service.deleteTripCard(Set.of(mine));
        em.flush();
        em.clear();

        assertThat(em.find(TripCardEntity.class, mine)).isNull();
        assertThat(children(mine)).isZero();
        assertThat(children(other)).isEqualTo(2);
    }
}
