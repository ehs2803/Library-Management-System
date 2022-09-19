package com.ehs.library.sanction.repository;

import com.ehs.library.loan.entity.Loan;
import com.ehs.library.member.entity.Member;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.constant.SanctionType;
import com.ehs.library.sanction.entity.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanctionRepository extends JpaRepository<Sanction, Long> {

    @Query("select s from Sanction s " +
            "join fetch s.loan l join fetch l.book " +
            "where s.member=:member and s.type='BOOK' " +
            "order by s.regTime desc ")
    List<Sanction> findBookSanctionsByMemberFetchJoin(@Param("member") Member member);

    @Query("select s from Sanction s " +
            "join fetch s.loan l join fetch l.book " +
            "where s.member=:member and s.type='STUDYROOM' " +
            "order by s.regTime desc ")
    List<Sanction> findStudyRoomSanctionsByMemberFetchJoin(@Param("member") Member member);

    Sanction findByLoan(Loan loan);
}
