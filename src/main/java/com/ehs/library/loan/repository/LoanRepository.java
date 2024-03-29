package com.ehs.library.loan.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("select l from Loan l join fetch l.book where l.member=:member and l.loanState=:loanState")
    List<Loan> findByMemberAndLoanFetchJoinBook(@Param("member") Member member, @Param("loanState") LoanState loanState);

    @Query("select l from Loan l join fetch l.book where l.member=:member and (l.loanState='LOAN' or l.loanState='OVERDUE')")
    List<Loan> findLoanAndOverdueByMemberFetchJoinBook(@Param("member") Member member);

    @Query("select l from Loan l join fetch l.book join fetch l.member where l.id=:id")
    Loan findByIdFetchJoin(@Param("id") Long id);

    @Query("select l from Loan l join fetch  l.member where l.loanState=:loanState")
    List<Loan> findByLoanStateFetchJoinMember(@Param("loanState") LoanState loanState);

    Loan findByBookAndLoanState(Book book, LoanState loanState);

    Boolean existsLoanByMemberAndLoanState(Member member, LoanState loanState);

    Boolean existsLoanByBookAndLoanState(Book book, LoanState loanState);

    int countLoanByMemberAndLoanState(Member member, LoanState loanState);

    @Query("select l from Loan l join fetch  l.book where l.member=:member")
    List<Loan> findByMemberFetchJoinBook(@Param("member") Member member);

    Boolean existsByMemberAndBookAndLoanState(Member member, Book book, LoanState loanState);
}
