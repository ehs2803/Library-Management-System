package com.ehs.library.loan.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.loan.repository.LoanRepository;
import com.ehs.library.loan.repository.LoanWaitListRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {

    private final BookRepository bookRepository;
    private final LoanWaitListRepository loanWaitListRepository;
    private final LoanRepository loanRepository;

    public void moveWaitList(Member member, Long bid){
        Book book = bookRepository.findById(bid).get();
        book.setState(BookState.WAIT);
        LoanWaitList loanWaitList = new LoanWaitList(member, book);

        loanWaitListRepository.save(loanWaitList);
    }

    public void deleteWaitList(Long wid){
        LoanWaitList loanWaitList = loanWaitListRepository.findByIdFetchJoin(wid);
        Book book = loanWaitList.getBook();
        book.setState(BookState.AVAILABLE);

        loanWaitListRepository.delete(loanWaitList);
    }

    public List<LoanWaitList> findByMember(Member member){
        return loanWaitListRepository.findByMember(member);
    }

    public List<Loan> findByMemberAndLoan(Member member, LoanState loanState){
        return loanRepository.findByMemberAndLoan(member, loanState);
    }

}
