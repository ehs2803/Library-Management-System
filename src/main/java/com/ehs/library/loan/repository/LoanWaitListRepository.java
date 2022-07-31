package com.ehs.library.loan.repository;

import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanWaitListRepository extends JpaRepository<LoanWaitList, Long> {
    @Query("select wait from LoanWaitList wait join fetch wait.book where wait.id=:id")
    LoanWaitList findByIdFetchJoin(@Param("id")Long id);

    @Query("select wait from LoanWaitList wait join fetch wait.book where wait.member=:member")
    List<LoanWaitList> findByMember(@Param("member") Member member);
}
