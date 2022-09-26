package com.ehs.library.loan.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookreservation.exception.BookReservationOverException;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.loan.exception.BookLoanSanctionException;
import com.ehs.library.loan.repository.LoanRepository;
import com.ehs.library.loan.repository.LoanWaitListRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class LoanServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private LoanWaitListRepository loanWaitListRepository;
    @Autowired private LoanRepository loanRepository;

    @Autowired private LoanService loanService;

    @Test
    @Order(1)
    @DisplayName("데이터베이스 저장")
    @Rollback(value = false)
    void save(){
        Member saveMember1 = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.USER)
                .build();
        Member saveMember2 = Member.builder()
                .name("test2")
                .email("test2@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .sanctionBookDay(3)
                .build();
        memberRepository.save(saveMember1);
        memberRepository.save(saveMember2);

        Book book1 = Book.builder()
                .name("book1")
                .isbn("isbn1")
                .author("author1")
                .publisher("publisher1")
                .year(2022)
                .price(10000)
                .page(100)
                .content("content")
                .register_numer("1")
                .symbol("1")
                .classification("1")
                .classification_detail("1")
                .state(BookState.AVAILABLE)
                .location("location")
                .member(saveMember1)
                .build();
        Book book2 = Book.builder()
                .name("book2")
                .isbn("isbn2")
                .author("author2")
                .publisher("publisher2")
                .year(2022)
                .price(10000)
                .page(100)
                .content("content")
                .register_numer("2")
                .symbol("2")
                .classification("2")
                .classification_detail("2")
                .state(BookState.AVAILABLE)
                .location("location")
                .member(saveMember2)
                .build();
        bookRepository.save(book1);
        bookRepository.save(book2);
    }

    @Test
    @Order(2)
    @DisplayName("대출 대기 목록으로 이동")
    @Rollback(value = false)
    void moveWaitList() {
        // given
        loanService.moveWaitList(memberRepository.findByEmail("test1@naver.com"), bookRepository.findBySymbol("1").getId());

        // when
        LoanWaitList loanWaitList = loanWaitListRepository.findByIdFetchJoin(1L);

        // then
        assertEquals("book1", loanWaitList.getBook().getName());
        assertEquals(BookState.WAIT, loanWaitList.getBook().getState());
    }

    @Test
    @Order(3)
    @DisplayName("멤버 기준 대출 대기 목록 조회")
    @Rollback(value = false)
    void findByMember() {
        // when
        List<LoanWaitList> loanWaitListList = loanService.findByMember(memberRepository.findByEmail("test1@naver.com"));

        // then
        assertEquals(1, loanWaitListList.size());
    }

    @Test
    @Order(4)
    @DisplayName("대출 대기 목록에서 삭제")
    @Rollback(value = false)
    void deleteWaitList() {
        // given
        loanService.deleteWaitList(1L);

        // when
        long cnt = loanWaitListRepository.count();

        // then
        assertEquals(0, cnt);
    }

    @Test
    @Order(5)
    @DisplayName("대출 하기 성공")
    @Rollback(value = false)
    void loanWatiBookList() {
        // given
        loanService.moveWaitList(memberRepository.findByEmail("test1@naver.com"), bookRepository.findBySymbol("2").getId());
        loanService.loanWatiBookList(memberRepository.findByEmail("test1@naver.com"));

        // when
        List<Loan> loanList = loanService.findLoanAndOverdueByMemberFetchJoinBook(memberRepository.findByEmail("test1@naver.com"));

        // then
        assertEquals(1, loanList.size());
        assertEquals("book2", loanList.get(0).getBook().getName());
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("반납기한 연장")
    void delayLoan() {
        // when
        List<Loan> loanList = loanService.findLoanAndOverdueByMemberFetchJoinBook(memberRepository.findByEmail("test1@naver.com"));
        loanService.delayLoan(loanList.get(0).getId());
        List<Loan> updateLoanList = loanService.findLoanAndOverdueByMemberFetchJoinBook(memberRepository.findByEmail("test1@naver.com"));

        // then
        assertEquals(1, updateLoanList.get(0).getUseExtensionCnt());
        assertEquals(21, updateLoanList.get(0).getRemainDay());
    }

    @Test
    @Order(7)
    @Rollback(value = true)
    @DisplayName("도서 분실 처리")
    void bookLoss() {
        // when
        List<Loan> loanList = loanService.findLoanAndOverdueByMemberFetchJoinBook(memberRepository.findByEmail("test1@naver.com"));
        loanService.BookLoss(loanList.get(0).getId());
        Book book = bookRepository.findById(bookRepository.findBySymbol("2").getId()).get();

        // then
        assertEquals(BookState.LOSS, book.getState());
    }


    @Test
    @Order(8)
    @Rollback(value = false)
    @DisplayName("도서 반납")
    void loanReturn() {
        // given
        loanService.moveWaitList(memberRepository.findByEmail("test1@naver.com"), bookRepository.findBySymbol("2").getId());
        loanService.loanWatiBookList(memberRepository.findByEmail("test1@naver.com"));

        // when
        List<Loan> loanList = loanService.findLoanAndOverdueByMemberFetchJoinBook(memberRepository.findByEmail("test1@naver.com"));
        loanService.loanReturn(loanList.get(0).getId());
        Book book = bookRepository.findById(bookRepository.findBySymbol("2").getId()).get();
        long cnt = loanRepository.countLoanByMemberAndLoanState(memberRepository.findByEmail("test1@naver.com"), LoanState.LOAN);

        // then
        assertEquals(BookState.AVAILABLE, book.getState());
        assertEquals(0, cnt);
    }

    @Test
    @Order(9)
    @DisplayName("도서 대출 예외 - 제재 중")
    void loanException(){
        // given
        loanService.moveWaitList(memberRepository.findByEmail("test2@naver.com"), bookRepository.findBySymbol("1").getId());


        // when
        BookLoanSanctionException ex = assertThrows(BookLoanSanctionException.class,
                () -> loanService.loanWatiBookList(memberRepository.findByEmail("test2@naver.com")));

        // then
        assertThat(ex.getMessage().equals("현재 제재중입니다. 도서 대출이 불가능합니다.")).isTrue();
    }



}