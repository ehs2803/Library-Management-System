package com.ehs.library.member.service;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 회원가입
    public Member saveMember(Member member){
        validateDuplicateMember(member); // 이메일 중복 검증
        return memberRepository.save(member); // 회원저장
    }

    // 이미 존재하는 이메일인지 검증
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }
    }

    // 이메일 기반 사용자 검색
    @Transactional(readOnly = true)
    public Member findByemail(String email){
        return memberRepository.findByEmail(email);
    }

    // 멤버 id 기반 사용자 검색
    @Transactional(readOnly = true)
    public Member findById(Long id){
        return memberRepository.findById(id).get();
    }

    // 멤버 정보 수정
    public Long updateMember(Member member){
        Member updateMember = memberRepository.findByEmail(member.getEmail());
        updateMember.setName(member.getName());
        updateMember.setEmail(member.getEmail());
        updateMember.setTel(member.getTel());
        updateMember.setPassword(member.getPassword());
        updateMember.setAddress(member.getAddress());
        return updateMember.getId();
    }

//    @Transactional(readOnly = true)
//    public List<Member> findByRole(Role role){
//        return memberRepository.findByRole(role);
//    }

    @Transactional(readOnly = true)
    public Page<Member> memberUserLoanList(String email, Pageable pageable) {
        return memberRepository.findByRoleAndEmailContaining(Role.USER, email, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}