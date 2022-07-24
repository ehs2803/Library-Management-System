package com.ehs.library.member.service;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
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

    public Member findByemail(String email){
        return memberRepository.findByEmail(email);
    }

    public Member findById(Long id){
        return memberRepository.findById(id).get();
    }

    public Long updateMember(Member member){
        Member updateMember = memberRepository.findByEmail(member.getEmail());
        updateMember.setName(member.getName());
        updateMember.setEmail(member.getEmail());
        updateMember.setTel(member.getTel());
        updateMember.setPassword(member.getPassword());
        updateMember.setAddress(member.getAddress());
        return updateMember.getId();
    }

    public List<Member> findByRole(Role role){
        return memberRepository.findByRole(role);
    }
}