package com.ehs.library.repository;

import com.ehs.library.constant.BookState;
import com.ehs.library.dto.MemberFormDto;
import com.ehs.library.entity.Book;
import com.ehs.library.entity.Member;
import com.ehs.library.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;


@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class BookRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new  MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("대한민국 서울시");
        memberFormDto.setPassword(password);
        memberFormDto.setRole("ADMIN");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("Book 생성 테스트")
    public void loginSuccessTest() throws Exception{
        Member member = createMember("temp@naver.com","1234");
        memberRepository.save(member);

        Book book = new Book();
        book.setName("책이름1");
        book.setIsbn("1");
        book.setAuthor("김작가");
        book.setPublisher("스프링출판사");
        book.setYear(2022);
        book.setRegister_numer("R1234");
        book.setSymbol("415.5 김25");
        book.setClassification("400");
        book.setClassification_detail("410");
        book.setMember(member);
        bookRepository.save(book);

        Book book1 = new Book();
        book1.setName("책이름2");
        book1.setIsbn("2");
        book1.setAuthor("김작가");
        book1.setPublisher("스프링출판사");
        book1.setYear(2022);
        book1.setRegister_numer("R1235");
        book1.setSymbol("415.5 김25-2");
        book1.setClassification("400");
        book1.setClassification_detail("410");
        book1.setMember(member);
        bookRepository.save(book1);

        em.flush();
        em.clear();

        Book findBook = bookRepository.findById(book.getId()).get();
        Assertions.assertEquals(findBook.getId(), book.getId());
        Assertions.assertEquals(findBook.getClassification(), book.getClassification());
        Assertions.assertEquals(findBook.getMember().getId(), member.getId());
    }

}