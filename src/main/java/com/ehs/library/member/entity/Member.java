package com.ehs.library.member.entity;

import com.ehs.library.base.entity.BaseTimeEntity;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.base.entity.BaseEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Builder
@Entity
@Table(name="member")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String tel;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setTel(memberFormDto.getTel());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        if(memberFormDto.getRole().equals("ADMIN")) {
            member.setRole(Role.ADMIN);
        }
        else {
            member.setRole(Role.USER);
        }
        return member;
    }

    public static Member createMember(MemberEditFormDto memberEditFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberEditFormDto.getName());
        member.setEmail(memberEditFormDto.getEmail());
        member.setTel(memberEditFormDto.getTel());
        member.setAddress(memberEditFormDto.getAddress());
        member.setPassword(passwordEncoder.encode(memberEditFormDto.getPassword()));
        return member;
    }

}