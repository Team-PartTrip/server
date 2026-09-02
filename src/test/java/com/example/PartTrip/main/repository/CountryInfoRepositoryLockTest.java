package com.example.PartTrip.main.repository;

import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Lock;

import static org.assertj.core.api.Assertions.assertThat;

class CountryInfoRepositoryLockTest {

    @Test
    void countryAcquisitionLookupUsesPessimisticWriteLock() throws NoSuchMethodException {
        Lock lock = CountryInfoRepository.class
                .getMethod(
                        "findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc",
                        String.class)
                .getAnnotation(Lock.class);

        assertThat(lock).isNotNull();
        assertThat(lock.value()).isEqualTo(LockModeType.PESSIMISTIC_WRITE);
    }
}
