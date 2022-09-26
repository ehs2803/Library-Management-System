package com.ehs.library.bookinterest.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.book.service.BookService;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.bookinterest.repository.BookInterestRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class BookInterestServiceTest {
    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookInterestService bookInterestService;
    @Autowired private BookInterestRepository bookInterestRepository;

    @BeforeEach
    public void save(){
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
                .content("aaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb" +
                        "vvvvvvvvvvvddddddddddddsssssssssseeeeee" +
                        "ffffffff")
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
                .content("aaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb" +
                        "vvvvvvvvvvvddddddddddddsssssssssseeeeee" +
                        "ffffffff")
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
                .content("aaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb" +
                        "vvvvvvvvvvvddddddddddddsssssssssseeeeee" +
                        "ffffffff")
                .register_numer("3")
                .symbol("3")
                .classification("2")
                .classification_detail("2")
                .state(BookState.AVAILABLE)
                .location("location")
                .member(saveMember2)
                .build();
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    @Test
    @Order(1)
    @DisplayName("관심도서 추가")
    void insertBookInterest() {
        // given
        BookInterest bookInterest = new BookInterest(bookRepository.findById(1L).get(),
                memberRepository.findByEmail("test1@naver.com"), LocalDateTime.now());
        bookInterestRepository.save(bookInterest);

        // when
        BookInterest findBookInterest = bookInterestRepository.findById(1L).get();

        // then
        assertSame(bookInterest, findBookInterest);
        assertEquals("book1", findBookInterest.getBook().getName());
    }

    @Test
    @Order(2)
    @DisplayName("관심도서 삭제")
    void deleteBookInterest() {
        // given
        BookInterest bookInterest = new BookInterest(bookRepository.findById(4L).get(),
                memberRepository.findByEmail("test1@naver.com"), LocalDateTime.now());
        bookInterestRepository.save(bookInterest);
        bookInterestService.deleteBookInterest(2L);

        // when
        //Optional<BookInterest> findBookInterest = bookInterestRepository.findById(1L);
        long cnt = bookInterestRepository.count();

        // then
        assertEquals(0,cnt);
    }
}