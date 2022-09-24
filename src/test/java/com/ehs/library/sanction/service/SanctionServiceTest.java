package com.ehs.library.sanction.service;

import com.ehs.library.base.constant.Policy;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import com.ehs.library.notice.service.NoticeService;
import com.ehs.library.sanction.constant.SanctionType;
import com.ehs.library.sanction.entity.Sanction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class SanctionServiceTest {
    @Autowired
    private SanctionService sanctionService;
    @Autowired
    private MemberService memberService;
    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void saveMembersSanctions(){
        Member saveMember1 = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.USER)
                .sanctionBookDay(5)
                .build();
        Member saveMember2 = Member.builder()
                .name("test2")
                .email("test2@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .sanctionStudyRoomDay(7)
                .build();
        memberService.saveMember(saveMember1);
        memberService.saveMember(saveMember2);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("제재 날짜 1일 줄이기 테스트")
    void decreaseRemainDay() {
        // given
        Member member_book = memberService.findByemail("test1@naver.com");
        Member member_studyroom = memberService.findByemail("test2@naver.com");

        assertEquals(5, member_book.getSanctionBookDay());
        assertEquals(7, member_studyroom.getSanctionStudyRoomDay());

        // when
        sanctionService.decreaseRemainDay();

        // then
        assertEquals(4, member_book.getSanctionBookDay());
        assertEquals(6, member_studyroom.getSanctionStudyRoomDay());
    }
}