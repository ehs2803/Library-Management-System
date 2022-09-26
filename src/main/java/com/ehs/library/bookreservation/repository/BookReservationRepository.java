package com.ehs.library.bookreservation.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long>{
    public int countBookReservationByBook(Book book);

    public int countBookReservationByMember(Member member);

    @EntityGraph(attributePaths = {"book"})
    public List<BookReservation> findAll();

//    @EntityGraph(attributePaths = {"book"})
//    public List<BookReservation> findByMember(Member member);

    @Query("select br from BookReservation br join fetch br.book b join fetch b.bookImg where br.member=:member")
    public List<BookReservation> findByMember(@Param("member") Member member);

    boolean existsByMemberAndBook(Member member, Book book);

    BookReservation findByMemberAndBook(Member member, Book book);

    boolean existsByBook(Book book);

}
