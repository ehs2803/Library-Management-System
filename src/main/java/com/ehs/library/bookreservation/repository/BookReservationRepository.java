package com.ehs.library.bookreservation.repository;

import com.ehs.library.bookreservation.entity.BookReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long>{

}
