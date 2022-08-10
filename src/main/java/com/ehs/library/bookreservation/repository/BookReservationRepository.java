package com.ehs.library.bookreservation.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long>{
    public int countBookReservationByBook(Book book);

    @EntityGraph(attributePaths = {"book"})
    public List<BookReservation> findByMember(Member member);
}
