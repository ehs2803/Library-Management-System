package com.ehs.library.loan.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.loan.entity.LoanWaitList;
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

    public void moveWaitList(Member member, Long bid){
        Book book = bookRepository.findById(bid).get();
        book.setState(BookState.WAIT);
        LoanWaitList loanWaitList = new LoanWaitList(member, book);

        loanWaitListRepository.save(loanWaitList);
    }

    public List<LoanWaitList> findByMember(Member member){
        return loanWaitListRepository.findByMember(member);
    }

}
