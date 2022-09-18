package com.ehs.library.member.controller;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
//@WebMvcTest(MemberController.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @BeforeEach
//    void before(WebApplicationContext was) {
//        mockMvc = MockMvcBuilders.webAppContextSetup(was)
//                .alwaysDo(print())
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//
//    }

    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new  MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        memberFormDto.setRole("ADMIN");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 페이지")
    public void loginPageAccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/member/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/loginForm"));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/member/login")
                        .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/member/login")
                        .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

//    @Test
//    @DisplayName("회원가입 테스트")
//    public void signupTest() throws Exception{
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setAddress("address1");
//        memberFormDto.setName("test1");
//        memberFormDto.setEmail("test1@naver.com");
//        memberFormDto.setPassword("test123456");
//        memberFormDto.setRole("ADMIN");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/member/signup")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(memberFormDto)))
//                .andExpect(status().is4xxClientError());
//    }
}