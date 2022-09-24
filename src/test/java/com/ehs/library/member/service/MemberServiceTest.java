package com.ehs.library.member.service;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.exception.UserAlreadyExistException;
import com.ehs.library.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("이현수");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("temp123456");
        memberFormDto.setRole("ADMIN");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @BeforeEach
    public void saveMembers(){
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
        memberService.saveMember(saveMember1);
        memberService.saveMember(saveMember2);
    }

    @DisplayName("회원저장 테스트")
    @Test
    void saveMemberTest(){
        assertEquals(2, memberRepository.count());

        Member saveMember3 = Member.builder()
                .name("test3")
                .email("test3@naver.com")
                .password("test123456")
                .address("tempAddress3")
                .role(Role.USER)
                .build();
        memberService.saveMember(saveMember3);

        assertEquals(3, memberRepository.count());
    }

    @DisplayName("이미 존재하는 이메일 검증")
    @Test
    public void validateDuplicateMemberTest(){
        Member saveMember = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.USER)
                .build();

        UserAlreadyExistException ex = assertThrows(UserAlreadyExistException.class, () -> memberService.saveMember(saveMember));

        assertThat(ex.getMessage().contains("이미 존재하는 이메일입니다")).isTrue();
    }

    @DisplayName("이메일 기반 사용자 검색")
    @Test
    public void findByEmailTest(){
        Member findMember = memberService.findByemail("test1@naver.com");

        assertEquals("test1", findMember.getName());
        assertEquals("test123456", findMember.getPassword());
        assertEquals("tempAddress", findMember.getAddress());
        assertEquals(null, findMember.getTel());
        assertEquals(Role.USER, findMember.getRole());
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest2(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(UserAlreadyExistException.class, () -> {
            memberService.saveMember(member2);});
        assertEquals("이미 존재하는 이메일입니다", e.getMessage());
    }
}