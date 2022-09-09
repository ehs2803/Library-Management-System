package com.ehs.library.loan.service;

import com.ehs.library.base.constant.Policy;
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

import java.time.LocalDateTime;
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

    public void loanWatiBookList(Member member){
        List<LoanWaitList> loanWaitListList = loanWaitListRepository.findByMember(member);
        // loan wati list
        for(int i=0;i<loanWaitListList.size();i++){
            Book book = loanWaitListList.get(i).getBook();
            book.setState(BookState.LOAN);
            book.setLoanCnt(book.getLoanCnt()+1);
            Loan loan = new Loan(book, member, LoanState.LOAN, LocalDateTime.now());
            loan.setRemainDay(Policy.LOAN_BOOK_DAY);
            loan.setUseExtensionCnt(0);
            loan.setOverdueDay(0);
            loanWaitListRepository.delete(loanWaitListList.get(i));
            loanRepository.save(loan);
        }
    }

    public Long loanReturn(Long id){
        Loan loan = loanRepository.findByIdFetchJoin(id);

        // book
        Book book = loan.getBook();
        book.setState(BookState.AVAILABLE);

        // loan state
        if(loan.getLoanState().toString().equals("LOAN")){
            loan.setLoanState(LoanState.NORMAL_RETURN);
        }
        else{
            loan.setLoanState(LoanState.OVERDUE_RETURN);
        }
        loan.setReturnTime(LocalDateTime.now());

        return loan.getMember().getId();
    }

    public void updateLoanState(){
        List<Loan> loanList_LOAN = loanRepository.findByLoanState(LoanState.LOAN);
        List<Loan> loanList_OVERDUE = loanRepository.findByLoanState(LoanState.OVERDUE);

        for(int i=0;i<loanList_LOAN.size();i++){
            Loan loan = loanList_LOAN.get(i);
            loan.setRemainDay(loan.getRemainDay()-1);
            if(loan.getRemainDay()==0){
                loan.setLoanState(LoanState.OVERDUE);
            }
        }

        for(int i=0;i<loanList_OVERDUE.size();i++){
            Loan loan = loanList_OVERDUE.get(i);
            loan.setOverdueDay(loan.getOverdueDay()+1);
        }
    }

    // 대출도서 대출기간 연장
    public void delayLoan(Long id) throws Exception {
        Loan loan = loanRepository.findById(id).get();
        if(loan.getUseExtensionCnt()==Policy.LOAN_BOOK_EXTENSION_CNT){
            throw new Exception("도서 연장횟수가 초과되었습니다.");
        }
        loan.setUseExtensionCnt(loan.getUseExtensionCnt()+1);
        loan.setRemainDay(loan.getRemainDay()+Policy.LOAN_BOOK_EXTENSION_DAY);
    }
}
