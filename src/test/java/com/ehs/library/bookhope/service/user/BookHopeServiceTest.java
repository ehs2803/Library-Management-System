package com.ehs.library.bookhope.service.user;

import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeRepository;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class BookHopeServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private BookHopeRepository bookHopeRepository;

    @Autowired private BookHopeService bookHopeService;

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
        memberRepository.save(saveMember1);
        memberRepository.save(saveMember2);
    }


    @Test
    @Order(1)
    @DisplayName("희망도서 신청 등록")
    void registerBookHope() {
        // given
        BookHopeFormDto bookHopeFormDto = new BookHopeFormDto();
        bookHopeFormDto.setName("title1");
        bookHopeFormDto.setIsbn("1234");
        bookHopeFormDto.setAuthor("author1");
        bookHopeFormDto.setPublisher("publisher1");
        bookHopeFormDto.setYear(2022);

        // when
        Long id = bookHopeService.registerBookHope(bookHopeFormDto, "test1@naver.com");

        // then
        BookHope findBookHope = bookHopeRepository.findById(id).get();
        assertEquals("title1", findBookHope.getName());
        assertEquals("1234", findBookHope.getIsbn());
        assertEquals("author1", findBookHope.getAuthor());
        assertEquals("publisher1", findBookHope.getPublisher());
        assertEquals(2022, findBookHope.getYear());
    }

    @Test
    @Order(2)
    @DisplayName("희망도서 신청 등록 조회")
    void findByMemberAndState() {
        // given
        BookHopeFormDto bookHopeFormDto = new BookHopeFormDto();
        bookHopeFormDto.setName("title1");
        bookHopeFormDto.setIsbn("1234");
        bookHopeFormDto.setAuthor("author1");
        bookHopeFormDto.setPublisher("publisher1");
        bookHopeFormDto.setYear(2022);
        bookHopeService.registerBookHope(bookHopeFormDto, "test1@naver.com");

        BookHopeFormDto bookHopeFormDto2 = new BookHopeFormDto();
        bookHopeFormDto2.setName("title1");
        bookHopeFormDto2.setIsbn("1234");
        bookHopeFormDto2.setAuthor("author1");
        bookHopeFormDto2.setPublisher("publisher1");
        bookHopeFormDto2.setYear(2022);
        bookHopeService.registerBookHope(bookHopeFormDto2, "test1@naver.com");

        // when
        List<BookHope> bookHopeList = bookHopeService.findByMemberAndState("test1@naver.com", BookHopeState.REVIEW);

        // then
        assertEquals(2, bookHopeList.size());
    }

}