package com.ehs.library.book.service;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.dto.BookDto;
import com.ehs.library.book.dto.BookSearchCondition;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.repository.BookImgRepository;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.loan.repository.LoanRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class BookServiceTest {

    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookService bookService;

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
    @DisplayName("도서 저장 테스트")
    void saveItem() {
        // when
        long cnt = bookRepository.count();

        // then
        assertEquals(3, cnt);
    }


    @Test
    @DisplayName("id 기반 도서 검색")
    void findBookById() {
        // when
        Book findBook = bookRepository.findById(1L).get();

        // then
        assertEquals("book1", findBook.getName());
        assertEquals("isbn1", findBook.getIsbn());
    }

    @Test
    @DisplayName("isbn 기반 도서 검색")
    void findBookbyISBN() {
        // when
        List<Book> bookList = bookService.findBookbyISBN("isbn1");

        //then
        assertEquals(1, bookList.size());
        assertEquals("book1", bookList.get(0).getName());
        assertEquals("author1", bookList.get(0).getAuthor());
    }

    @Test
    void getCountByIsbn() {
        // when
        long cnt1 = bookService.getCountByIsbn("isbn1");
        long cnt2 = bookService.getCountByIsbn("isbn2");

        // then
        assertEquals(1, cnt1);
        assertEquals(2, cnt2);
    }
}