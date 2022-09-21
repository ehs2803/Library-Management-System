package com.ehs.library.member.repository;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;


//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest(properties = "spring.config.location=classpath:application-test.yml")
@SpringBootTest
@Transactional
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

//    @PersistenceContext //@Autowired
//    EntityManager em;
//
//    JPAQueryFactory queryFactory;
//
//    @BeforeEach
//    public void init() {
//        queryFactory = new JPAQueryFactory(em);
//    }

    @DisplayName("email로 Member찾기")
    @Test
    void findByEmailTest() {
        // given
        Member saveMember = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.USER)
                .build();
        memberRepository.save(saveMember);

        //  when
        Member result = memberRepository.findByEmail("test1@naver.com");

        // then
        assertThat(result.getEmail()).isEqualTo("test1@naver.com");
    }



}