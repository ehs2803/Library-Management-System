package com.ehs.library.roomreservation.repository;

import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRoomReservationRepository extends JpaRepository<StudyRoomReservation, Long> {

}
