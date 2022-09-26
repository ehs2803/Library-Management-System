package com.ehs.library.bookreservation.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.bookreservation.exception.BookReservationAlreadyException;
import com.ehs.library.bookreservation.exception.BookReservationLimitException;
import com.ehs.library.bookreservation.exception.BookReservationOverException;
import com.ehs.library.bookreservation.exception.BookReservationSanctionException;
import com.ehs.library.bookreservation.repository.BookReservationRepository;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.exception.UserAlreadyExistException;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class BookReservationServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private BookReservationRepository bookReservationRepository;

    @Autowired private BookReservationService bookReservationService;

    @BeforeEach
    public void save(){
        Member saveMember1 = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.USER)
                .sanctionBookDay(3)
                .build();
        Member saveMember2 = Member.builder()
                .name("test2")
                .email("test2@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .build();
        Member saveMember3 = Member.builder()
                .name("test3")
                .email("test3@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .build();
        Member saveMember4 = Member.builder()
                .name("test4")
                .email("test4@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .build();
        Member saveMember5 = Member.builder()
                .name("test5")
                .email("test5@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .build();
        memberRepository.save(saveMember1);
        memberRepository.save(saveMember2);
        memberRepository.save(saveMember3);
        memberRepository.save(saveMember4);
        memberRepository.save(saveMember5);

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
        Book book3 = Book.builder()
                .name("book2")
                .isbn("isbn2")
                .author("author2")
                .publisher("publisher2")
                .year(2022)
                .price(10000)
                .page(100)
                .content("content")
                .register_numer("3")
                .symbol("3")
                .classification("2")
                .classification_detail("2")
                .state(BookState.AVAILABLE)
                .location("location")
                .member(saveMember2)
                .build();
        Book book4 = Book.builder()
                .name("book2")
                .isbn("isbn2")
                .author("author2")
                .publisher("publisher2")
                .year(2022)
                .price(10000)
                .page(100)
                .content("content")
                .register_numer("4")
                .symbol("4")
                .classification("2")
                .classification_detail("2")
                .state(BookState.AVAILABLE)
                .location("location")
                .member(saveMember2)
                .build();
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
    }

    @Test
    @Order(1)
    @DisplayName("도서예약 실패 - 제재")
    void reservationBook1() {
        // when
        BookReservationSanctionException ex = assertThrows(BookReservationSanctionException.class,
                () -> bookReservationService.reservationBook("test1@naver.com",bookRepository.findBySymbol("1").getId()));

        // then
        assertThat(ex.getMessage().equals("회원님은 현재 도서연체로 제재중입니다. 도서 예약이 불가능합니다.")).isTrue();
    }

    @Test
    @Order(2)
    @DisplayName("도서예약 실패 - 이미 예약")
    void reservationBook2() {
        // given
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("1").getId());

        // when
        BookReservationAlreadyException ex = assertThrows(BookReservationAlreadyException.class,
                () -> bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("1").getId()));

        // then
        assertThat(ex.getMessage().equals("회원님은 이미 해당 도서를 예약했습니다.")).isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("도서예약 실패 - 예약가능횟수 초과")
    void reservationBook3() {
        // given
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("1").getId());
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("2").getId());
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("3").getId());

        // when
        BookReservationOverException ex = assertThrows(BookReservationOverException.class,
                () -> bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("4").getId()));

        // then
        assertThat(ex.getMessage().equals("회원님은 예약가능 횟수를 초과했습니다.")).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("도서예약 실패 - 예약인원 초과")
    void reservationBook4() {
        // given
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("1").getId());
        bookReservationService.reservationBook("test3@naver.com", bookRepository.findBySymbol("1").getId());
        bookReservationService.reservationBook("test4@naver.com", bookRepository.findBySymbol("1").getId());

        // when
        BookReservationLimitException ex = assertThrows(BookReservationLimitException.class,
                () -> bookReservationService.reservationBook("test5@naver.com", bookRepository.findBySymbol("1").getId()));

        // then
        assertThat(ex.getMessage().equals("이 책은 현재 3명의 회원이 예약했습니다. 더이상 예약이 불가능합니다.")).isTrue();
    }

    @Test
    @Order(5)
    @DisplayName("도서예약 실패 - 대출 중")
    void reservationBook5() {
        // given

    }

    @Test
    @Order(6)
    @DisplayName("도서예약 성공")
    void reservationBook6() {
        // given
        bookReservationService.reservationBook("test5@naver.com", bookRepository.findBySymbol("1").getId());

        // when
        BookReservation bookReservation = bookReservationRepository.findByMemberAndBook(memberRepository.findByEmail("test5@naver.com"),
                bookRepository.findBySymbol("1"));

        // then
        //assertEquals(1, bookReservation.size());
        assertEquals("book1", bookReservation.getBook().getName());
    }

    @Test
    @Order(7)
    @DisplayName("도서예약 취소")
    void deleteReservation() {
        // given
        bookReservationService.reservationBook("test2@naver.com", bookRepository.findBySymbol("1").getId());

        // when
        List<BookReservation> bookReservation = bookReservationRepository.findByMember(memberRepository.findByEmail("test2@naver.com"));

        // then
        assertEquals(0, bookReservation.size());
    }

}