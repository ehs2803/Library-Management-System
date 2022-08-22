package com.ehs.library.base.repository;

import com.ehs.library.base.entity.HitPerDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HitPerDayRepository extends JpaRepository<HitPerDay, Long> {
    HitPerDay findByDay(LocalDate localDate);

    Long countByDay(LocalDate localDate);
}
