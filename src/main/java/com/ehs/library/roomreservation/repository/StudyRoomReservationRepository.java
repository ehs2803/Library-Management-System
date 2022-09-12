package com.ehs.library.roomreservation.repository;

import com.ehs.library.member.entity.Member;
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

    @Query("select sr from StudyRoomReservation sr join fetch sr.room r where sr.state='USE'")
    List<StudyRoomReservation> findByStateUseFetchJoinRoom();

    @Query("select sr from StudyRoomReservation sr join fetch sr.room r where sr.id=:id")
    StudyRoomReservation findByIdFetchJoinRoom(@Param("id") Long id);

    @Query("select sr from StudyRoomReservation sr join fetch sr.room r where sr.member=:member")
    List<StudyRoomReservation> findByMemberFetchJoinRoom(@Param("member") Member member);
}
