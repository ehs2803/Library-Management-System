package com.ehs.library.bookinterest.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInterestRepository extends JpaRepository<BookInterest, Long> {
//    @EntityGraph(attributePaths = {"book"})
//    public List<BookInterest> findByMember(Member member);

    @Query("select bi from BookInterest  bi join fetch bi.book b join fetch b.bookImg where bi.member=:member")
    public List<BookInterest> findByMember(@Param("member") Member member);

    public int countBookInterestByMemberAndBook(Book book, Member member);
}
