package com.ehs.library.loan.service;

import com.ehs.library.base.constant.Policy;
import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.loan.exception.BookLoanExtensionLimitException;
import com.ehs.library.loan.exception.BookLoanLimitException;
import com.ehs.library.loan.exception.BookLoanOverdueException;
import com.ehs.library.loan.exception.BookLoanSanctionException;
import com.ehs.library.loan.repository.LoanRepository;
import com.ehs.library.loan.repository.LoanWaitListRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.constant.SanctionType;
import com.ehs.library.sanction.entity.Sanction;
import com.ehs.library.sanction.repository.SanctionRepository;
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
    private final SanctionRepository sanctionRepository;
    private final MemberRepository memberRepository;

    // 도서 ID : bid 도서 waitList로 이동
    public void moveWaitList(Member member, Long bid){
        Book book = bookRepository.findById(bid).get();
        book.setState(BookState.WAIT); // 책 상태 WAIT로 설정

        LoanWaitList loanWaitList = new LoanWaitList(member, book);
        loanWaitListRepository.save(loanWaitList);
    }

    // 대출대기 목록에 있는 도서 삭제하기
    public void deleteWaitList(Long wid){
        LoanWaitList loanWaitList = loanWaitListRepository.findByIdFetchJoin(wid);
        Book book = loanWaitList.getBook();
        book.setState(BookState.AVAILABLE); // 도서상태 AVAILABLE로 변경

        loanWaitListRepository.delete(loanWaitList);
    }

    // 특정 일반 유저 대출 대기 목록 조회
    public List<LoanWaitList> findByMember(Member member){
        return loanWaitListRepository.findByMemberFetchJoinBook(member);
    }

    // 대출 대기 목록 조회 by 유저, loanstate
    public List<Loan> findByMemberAndLoan(Member member, LoanState loanState){
        return loanRepository.findByMemberAndLoanFetchJoinBook(member, loanState);
    }

    // 대출 대기 목록에 있는 도서 대출처리
    public void loanWatiBookList(Member member) {
        List<LoanWaitList> loanWaitListList = loanWaitListRepository.findByMemberFetchJoinBook(member);

        // 제재 중일 때
        if(member.getSanctionBookDay()>0){
            throw new BookLoanSanctionException("현재 제재중입니다. 도서 대출이 불가능합니다.");
        }

        // 만약 연체중인 책이 있을 때
        if(loanRepository.existsLoanByMemberAndLoanState(member, LoanState.OVERDUE)){
            throw new BookLoanOverdueException("현재 연체된 책이 있습니다. 도서 대출이 불가능합니다.");
        }
        // 빌릴 수 있는 책의 최대 개수를 초과할 때
        if(loanWaitListList.size()>Policy.LOAN_BOOK_CNT-loanRepository.countLoanByMemberAndLoanState(member, LoanState.LOAN)){
            throw new BookLoanLimitException("빌릴 수 있는 책의 최대 개수를 초과했습니다.");
        }

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

    // 도서 반납하기
    public Long loanReturn(Long id){
        Loan loan = loanRepository.findByIdFetchJoin(id);

        // 도서 상태 변경
        Book book = loan.getBook();
        book.setState(BookState.AVAILABLE);

        // loan state 설정
        if(loan.getLoanState().toString().equals("LOAN")){ // 정상 반납
            loan.setLoanState(LoanState.NORMAL_RETURN);
        }
        else{
            loan.setLoanState(LoanState.OVERDUE_RETURN); // 연체 반납
        }
        loan.setReturnTime(LocalDateTime.now()); // 반납시간 현재 시간 설정

        return loan.getMember().getId();
    }

    // 대출도서 대출기간 연장
    public void delayLoan(Long id) {
        Loan loan = loanRepository.findById(id).get();
        if(loan.getUseExtensionCnt()==Policy.LOAN_BOOK_EXTENSION_CNT){
            throw new BookLoanExtensionLimitException("도서 연장횟수가 초과되었습니다.");
        }
        loan.setUseExtensionCnt(loan.getUseExtensionCnt()+1);
        loan.setRemainDay(loan.getRemainDay()+Policy.LOAN_BOOK_EXTENSION_DAY);
    }

    // 대출 상태 변경
    public void updateLoanState(){
        List<Loan> loanList_LOAN = loanRepository.findByLoanStateFetchJoinMember(LoanState.LOAN);
        List<Loan> loanList_OVERDUE = loanRepository.findByLoanStateFetchJoinMember(LoanState.OVERDUE);

        // 대출 중인 건에 대해서...
        for(int i=0;i<loanList_LOAN.size();i++){
            Loan loan = loanList_LOAN.get(i);
            loan.setRemainDay(loan.getRemainDay()-1); // RemainDay -1하기.
            if(loan.getRemainDay()==0){ // RemainDay가 0일 경우
                loan.setLoanState(LoanState.OVERDUE); // 대출상태 연체 상태로 변경
                loan.setOverdueDay(1); // 연체일 수 1로 설정
                Sanction sanction = Sanction.builder()
                        .member(loan.getMember())
                        .sanctionDay(Policy.SANCTION_DAY_BOOK)
                        .type(SanctionType.BOOK)
                        .loan(loan)
                        .build();
                sanctionRepository.save(sanction);
                Member member = loan.getMember();
                member.setSanctionBookDay(member.getSanctionBookDay()+Policy.SANCTION_DAY_BOOK);
            }
        }

        // 이미 연체 중인 대출 건에 대해서...
        for(int i=0;i<loanList_OVERDUE.size();i++){
            Loan loan = loanList_OVERDUE.get(i);
            loan.setOverdueDay(loan.getOverdueDay()+1); // 연체일수 +1

            Sanction sanction = sanctionRepository.findByLoan(loan);
            sanction.setSanctionDay(sanction.getSanctionDay()+Policy.SANCTION_DAY_BOOK);

            Member member = loan.getMember();
            member.setSanctionBookDay(member.getSanctionBookDay()+Policy.SANCTION_DAY_BOOK);
        }
    }
}
