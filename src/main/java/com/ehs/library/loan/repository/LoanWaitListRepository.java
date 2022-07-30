package com.ehs.library.loan.repository;

import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanWaitListRepository extends JpaRepository<LoanWaitList, Long> {
    List<LoanWaitList> findByMember(Member member);
}
