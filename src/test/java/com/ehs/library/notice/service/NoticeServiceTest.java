package com.ehs.library.notice.service;

import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import com.ehs.library.notice.dto.NoticeAddFormDto;
import com.ehs.library.notice.dto.NoticeDto;
import com.ehs.library.notice.dto.NoticeFormDto;
import com.ehs.library.notice.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class NoticeServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    NoticeService noticeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시");
        memberFormDto.setPassword("1234");
        memberFormDto.setRole("ADMIN");
        return Member.createMember(memberFormDto, passwordEncoder);
    }



    @Test
    @DisplayName("공지사항 작성 테스트")
    public void writeNoticeTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        Notice savedNotice = noticeService.saveNewNotice(new NoticeAddFormDto("test title", "test content"));
        Notice savedNotice2 = noticeService.saveNewNotice(new NoticeAddFormDto("test title2", "test content2"));

        em.flush();
        em.clear();

        List<NoticeFormDto> noticeFormDtoList = noticeService.findAllNoticeList();
        NoticeDto findNotice = noticeService.noticeDetail(savedNotice.getId());

        assertEquals("test title",findNotice.getTitle());
        assertEquals("test content",findNotice.getContent());
        assertEquals(1,findNotice.getId());
        assertEquals(1,findNotice.getHit());
        //assertEquals(findNotice.getMember().getEmail(), member.getEmail());

        assertEquals(noticeFormDtoList.size(), 2);
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    public void deleteNoticeTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        Notice savedNotice = noticeService.saveNewNotice(new NoticeAddFormDto("test title", "test content"));
        Notice savedNotice2 = noticeService.saveNewNotice(new NoticeAddFormDto("test title2", "test content2"));

        List<NoticeFormDto> noticeFormDtoList1 = noticeService.findAllNoticeList();
        assertEquals(2,noticeFormDtoList1.size());

        // 공지사항 한개 삭제
        noticeService.deleteNotice(savedNotice2.getId());
        List<NoticeFormDto> noticeFormDtoList2 = noticeService.findAllNoticeList();
        assertEquals(1,noticeFormDtoList2.size());

    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    public void editNoticeTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        Notice savedNotice = noticeService.saveNewNotice(new NoticeAddFormDto("test title", "test content"));
        Notice savedNotice2 = noticeService.saveNewNotice(new NoticeAddFormDto("test title2", "test content2"));

        NoticeDto findNotice = noticeService.noticeDetail(savedNotice.getId());
        findNotice.setTitle("test title update");
        findNotice.setContent("test content update");
        em.flush();
        em.clear();

        // 공지사항 수정
        NoticeDto updateNotice = noticeService.noticeDetail(savedNotice.getId());
        assertEquals("test title",updateNotice.getTitle());
        assertEquals("test content",updateNotice.getContent());
    }
}