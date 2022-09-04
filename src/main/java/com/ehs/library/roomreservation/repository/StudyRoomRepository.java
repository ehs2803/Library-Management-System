package com.ehs.library.roomreservation.repository;

import com.ehs.library.roomreservation.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    StudyRoom findByName(String name);

    @Query("select sr from StudyRoom sr join fetch sr.reservations rl " +
            "where sr.id=:id and (rl.state='WAIT' or rl.state='ALLOW') order by rl.reservation_time")
    StudyRoom findByIdFetchJoin(@Param("id") Long id);

    @Query("select sr from StudyRoom sr join fetch sr.reservations rl join fetch rl.member " +
            "where sr.id=:id order by rl.reservation_time")
    StudyRoom findByIdFetchJoinAll(@Param("id") Long id);
}
