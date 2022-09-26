package com.ehs.library.bookhope.service.admin;

import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.dto.BookHopeUpdateDto;
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
    @Autowired private com.ehs.library.bookhope.service.user.BookHopeService bookHopeServiceUser;

    @BeforeEach
    public void saveMembers(){
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
    @DisplayName("희망도서 신청 상태별 조회")
    void findByState() {
        // given
        BookHopeFormDto bookHopeFormDto = new BookHopeFormDto();
        bookHopeFormDto.setName("title1");
        bookHopeFormDto.setIsbn("1234");
        bookHopeFormDto.setAuthor("author1");
        bookHopeFormDto.setPublisher("publisher1");
        bookHopeFormDto.setYear(2022);
        bookHopeServiceUser.registerBookHope(bookHopeFormDto, "test1@naver.com");

        BookHopeFormDto bookHopeFormDto2 = new BookHopeFormDto();
        bookHopeFormDto2.setName("title2");
        bookHopeFormDto2.setIsbn("4321");
        bookHopeFormDto2.setAuthor("author2");
        bookHopeFormDto2.setPublisher("publisher2");
        bookHopeFormDto2.setYear(2022);
        bookHopeServiceUser.registerBookHope(bookHopeFormDto2, "test2@naver.com");

        // when
        List<BookHope> bookHopeList_review = bookHopeService.findByState(BookHopeState.REVIEW);
        List<BookHope> bookHopeList_allow = bookHopeService.findByState(BookHopeState.ALLOW);

        // then
        assertEquals(2, bookHopeList_review.size());
        assertEquals(0, bookHopeList_allow.size());
    }

    @Test
    @DisplayName("희망도서 신청 상태별 건수 조회")
    void countByState() {
        // given
        BookHopeFormDto bookHopeFormDto = new BookHopeFormDto();
        bookHopeFormDto.setName("title1");
        bookHopeFormDto.setIsbn("1234");
        bookHopeFormDto.setAuthor("author1");
        bookHopeFormDto.setPublisher("publisher1");
        bookHopeFormDto.setYear(2022);
        bookHopeServiceUser.registerBookHope(bookHopeFormDto, "test1@naver.com");

        BookHopeFormDto bookHopeFormDto2 = new BookHopeFormDto();
        bookHopeFormDto2.setName("title2");
        bookHopeFormDto2.setIsbn("4321");
        bookHopeFormDto2.setAuthor("author2");
        bookHopeFormDto2.setPublisher("publisher2");
        bookHopeFormDto2.setYear(2022);
        bookHopeServiceUser.registerBookHope(bookHopeFormDto2, "test2@naver.com");

        // when
        long cnt_review = bookHopeService.countByState(BookHopeState.REVIEW);
        long cnt_allow = bookHopeService.countByState(BookHopeState.ALLOW);

        // then
        assertEquals(2, cnt_review);
        assertEquals(0, cnt_allow);
    }


    @Test
    @DisplayName("희망도서 신청 상태 업데이트")
    void updateState() {
        // given
        BookHopeFormDto bookHopeFormDto = new BookHopeFormDto();
        bookHopeFormDto.setName("title1");
        bookHopeFormDto.setIsbn("1234");
        bookHopeFormDto.setAuthor("author1");
        bookHopeFormDto.setPublisher("publisher1");
        bookHopeFormDto.setYear(2022);
        Long regId = bookHopeServiceUser.registerBookHope(bookHopeFormDto, "test1@naver.com");

        BookHopeUpdateDto bookHopeUpdateDto = new BookHopeUpdateDto();
        bookHopeUpdateDto.setId(regId);
        bookHopeUpdateDto.setState(BookHopeState.ALLOW);

        // when
        Long id = bookHopeService.updateState(bookHopeUpdateDto);

        // then
        assertEquals(BookHopeState.ALLOW, bookHopeRepository.findById(id).get().getState());
    }
}