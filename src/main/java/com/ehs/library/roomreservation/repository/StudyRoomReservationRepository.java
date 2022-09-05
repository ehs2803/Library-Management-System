package com.ehs.library.roomreservation.repository;

import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRoomReservationRepository extends JpaRepository<StudyRoomReservation, Long> {
    @Query("select sr from StudyRoomReservation sr join fetch sr.room r " +
            "where sr.state='WAIT' order by sr.reservation_time")
    List<StudyRoomReservation> findByStateWaitFetchJoin();

    @Query("select sr from StudyRoomReservation sr join fetch sr.room r " +
            "where sr.state='ALLOW' order by sr.reservation_time")
    List<StudyRoomReservation> findByStateAllowFetchJoin();

    @Query("select sr from StudyRoomReservation sr join fetch sr.room r " +
            "where sr.state='COMPLETE' or sr.state='REJECT' order by sr.reservation_time")
    List<StudyRoomReservation> findByStateCompleteAndRejectFetchJoin();
}
